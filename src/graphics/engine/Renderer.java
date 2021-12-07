package graphics.engine;

import events.EventQueue;
import events.ThreadID;
import graphics.SpriteManager;
import graphics.geometry.Sprite;
import input.Keyboard;
import input.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static CONFIG.CONFIG.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
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
        this.window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, VSYNC);
        this.spriteManager = SpriteManager.createSpriteManager();
        this.camera = new Camera(new Vector3f(0.0f, 0.0f, 6.0f), 6.0f);
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

    public void run(){
        try{
            this.init();
        }catch(Exception e) {e.printStackTrace();}

        boolean running = true;

        //translations[99] = new Vector3f(-1.0f, -1.0f, -1.0f);
        while (running && !window.windowShouldClose()) {
            // ! INPUT ---------------------------------------------------------
            glfwPollEvents();
            if (Mouse.LMBPress) {
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
