package graphics.engine;

import events.EventQueue;
import events.ThreadID;
import graphics.SpriteManager;
import graphics.UI.UI;
import graphics.geometry.Sprite;
import input.Keyboard;
import input.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.nuklear.NkMouse;

import java.util.ArrayList;

import static CONFIG.CONFIG.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public class Renderer extends Thread {
    private EventQueue eventQueue;

    private final Window window;
    private Shader shader;
    private final Camera camera;
    private SpriteManager spriteManager;

    public Renderer() {
        UI.createUI();
        this.window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, VSYNC);
        this.spriteManager = SpriteManager.createSpriteManager();
        this.camera = new Camera(new Vector3f(0.0f, 0.0f, ENV_SIZE), ENV_SIZE);
        this.eventQueue = new EventQueue(ThreadID.Render);
    }

    public void init() throws Exception {
        this.window.init();

        this.shader = new Shader();
        this.shader.createFragmentShader(Util.loadResource("shaders/fragment.glsl"));
        this.shader.createVertexShader(Util.loadResource("shaders/vertex.glsl"));
        this.shader.link();

        this.spriteManager.init();
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
                    glBindVertexArray(sprite.mesh.getVaoID());
                    glEnableVertexAttribArray(0);

                    Matrix4f model = sprite.getModelMatrix();
                    try {
                        this.shader.setUniform("model", model);
                        this.shader.setUniform("color", sprite.mesh.getColor());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (sprite.wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                    else glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

                    glDrawElementsInstanced(GL_TRIANGLES, sprite.mesh.getVertexCount(), GL_UNSIGNED_INT, 0, geometry.size());
                    glBindVertexArray(0);
                }
            }
            shader.unbind();

            // ! DISPLAY -------------------------------------------------------
            UI.instance.render(NK_ANTI_ALIASING_ON, 512 * 1024, 128 * 1024);
            window.update();
        }
    }

    public void flush() {
        if (this.shader != null) {
            shader.flush();
        }
        //TODO: purger chaque mesh.
    }
}
