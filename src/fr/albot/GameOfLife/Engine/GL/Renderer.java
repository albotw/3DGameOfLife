package fr.albot.GameOfLife.Engine.GL;

import fr.albot.GameOfLife.Engine.SpriteManager;
import fr.albot.GameOfLife.Engine.UI.UI;
import fr.albot.GameOfLife.Engine.Util;
import fr.albot.GameOfLife.Engine.geometry.Sprite;
import fr.albot.GameOfLife.Engine.geometry.TexturedMesh;
import fr.albot.GameOfLife.Engine.input.Keyboard;
import fr.albot.GameOfLife.Engine.input.Mouse;
import fr.albot.GameOfLife.Engine.textures.TextureAtlas;
import fr.albot.GameOfLife.Engine.textures.TextureID;
import fr.albot.GameOfLife.events.Event;
import fr.albot.GameOfLife.events.EventQueue;
import fr.albot.GameOfLife.events.Events.InitGridEvent;
import fr.albot.GameOfLife.events.Events.PurgeEvent;
import fr.albot.GameOfLife.events.Events.SpriteUpdateDoneEvent;
import fr.albot.GameOfLife.events.Events.UpdateSpritesEvent;
import fr.albot.GameOfLife.events.ThreadID;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.nuklear.NkMouse;

import java.util.ArrayList;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.nk_input_begin;
import static org.lwjgl.nuklear.Nuklear.nk_input_end;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer extends Thread {
    private EventQueue eventQueue;

    private final Window window;
    private Shader shader;
    private final Camera camera;
    private SpriteManager spriteManager;
    private TextureAtlas textureAtlas;

    public Renderer() {
        this.eventQueue = new EventQueue(ThreadID.Render);
        UI.createUI();
        this.window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, VSYNC);
        this.textureAtlas = new TextureAtlas();
        this.spriteManager = SpriteManager.createSpriteManager();
        this.camera = new Camera(new Vector3f(0.0f, 0.0f, ENV_SIZE), ENV_SIZE);
    }

    public void init() throws Exception {
        this.window.init();
        this.textureAtlas.load();
        this.shader = new Shader();
        this.shader.createFragmentShader(Util.loadResource("ressources/shaders/fragment.glsl"));
        this.shader.createVertexShader(Util.loadResource("ressources/shaders/vertex.glsl"));
        this.shader.link();
    }


    public void run() {
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean running = true;

        //translations[99] = new Vector3f(-1.0f, -1.0f, -1.0f);
        while (running && !window.windowShouldClose()) {
            // ! INPUT ---------------------------------------------------------

            nk_input_begin(UI.context);
            glfwPollEvents();
            NkMouse mouse = UI.context.input().mouse();
            if (mouse.grab()) {
                glfwSetInputMode(this.window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
            } else if (mouse.grabbed()) {
                float prevX = mouse.prev().x();
                float prevY = mouse.prev().y();
                glfwSetCursorPos(this.window.getHandle(), prevX, prevY);
                mouse.pos().x(prevX);
                mouse.pos().y(prevY);
            } else if (mouse.ungrab()) {
                glfwSetInputMode(this.window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
            nk_input_end(UI.context);

            if (Mouse.LMBPress && !UI.instance.isMouseOnUI()) {
                camera.rotate(Mouse.Xoffset, Mouse.Yoffset);
            }
            if (Keyboard.UP_press || Keyboard.Z_press) {
                camera.zoomIn();
            }
            if (Keyboard.DOWN_press || Keyboard.S_press) {
                camera.zoomOut();
            }

            // ! UPDATE --------------------------------------------------------
            if (!this.eventQueue.isEmpty()) {
                Event e = this.eventQueue.get();
                if (e instanceof InitGridEvent) {
                    this.spriteManager.init();
                    System.out.println("done init");
                }
                if (e instanceof PurgeEvent) {
                    this.spriteManager.purge();
                    System.out.println("done purge");
                }
                if (e instanceof UpdateSpritesEvent) {
                    this.spriteManager.displayEnv();
                    this.eventQueue.send(new SpriteUpdateDoneEvent(), ThreadID.Server);
                    System.out.println("done update");
                }
            }

            // ! RENDER --------------------------------------------------------
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            this.shader.bind();
            try {
                this.shader.setUniform("proj", this.window.getProjectionMatrix());
                this.shader.setUniform("view", this.camera.getViewMatrix());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<Sprite> geometry = this.spriteManager.getGeometry();
            for (int i = 0; i < geometry.size(); i++) {
                Sprite sprite = geometry.get(i);

                if (!sprite.hidden) {
                    Matrix4f model = sprite.getModelMatrix();
                    try {
                        this.shader.setUniform("model", model);
                        //this.shader.setUniform("color", sprite.mesh.getColor());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (sprite.wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                    else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

                    glBindVertexArray(sprite.mesh.getVaoID());
                    glEnableVertexAttribArray(0);

                    if (!sprite.solid) {
                        glEnableVertexAttribArray(1);
                        TextureID texture = ((TexturedMesh) sprite.mesh).getTexture();
                        this.textureAtlas.bind(texture);
                    }

                    glDrawElements(GL_TRIANGLES, sprite.mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                    glBindVertexArray(0);
                }
            }
            shader.unbind();

            // ! DISPLAY -------------------------------------------------------
            //UI.instance.render(NK_ANTI_ALIASING_ON, 512 * 1024, 128 * 1024);
            window.update();
        }
        System.out.println("done rendering");
    }

    public void flush() {
        if (this.shader != null) {
            shader.flush();
        }
        //TODO: purger chaque mesh.
    }
}
