package graphics;

import input.Keyboard;
import input.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GLXNVDelayBeforeSwap;

import java.util.ArrayList;

import static CONFIG.CONFIG.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public class Renderer extends Thread{
    private Window window;
    private Shader shader;
    private ArrayList<Mesh> geometry;
    private Camera camera;

    public Renderer() {
        this.window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, VSYNC);
        this.geometry = new ArrayList<>();
        this.camera = new Camera(new Vector3f(0.0f, 0.0f, 5.0f), 5.0f);
    }

    public void run() {
        try {
            init();
            MeshGenerator.generate(this);
            render();
        }catch(Exception e) {e.printStackTrace();}
    }

    public void init() throws Exception {
        this.window.init();

        this.shader = new Shader();
        this.shader.createFragmentShader(Util.loadResource("shaders/fragment.glsl"));
        this.shader.createVertexShader(Util.loadResource("shaders/vertex.glsl"));
        this.shader.link();
        System.out.println("finished render init");
    }

    public void renderMesh(Mesh m) {
        this.geometry.add(m);
    }

    public void render() throws Exception {
        boolean running = true;
        System.out.println("started rendering");

        //translations[99] = new Vector3f(-1.0f, -1.0f, -1.0f);
        float angleX = 0.0f;
        float angleY = 0.0f;
        while(running && !window.windowShouldClose()) {
            //input
            glfwPollEvents();
            if (Keyboard.UP_press) {angleX = 0.1f;}
            if (Keyboard.DOWN_press) {angleX = -0.1f;}
            if (Keyboard.LEFT_press) {angleY = -0.1f;}
            if (Keyboard.RIGHT_press) {angleY = 0.1f;}
            if (Keyboard.ZERO_press) {angleX = 0.0f; angleY = 0.0f;}
            System.out.println(angleX + " | " + angleY);
            /**
             * if (Mouse.LMBPress) {
             *                 camera.rotate(Mouse.Xoffset, Mouse.Yoffset);
             *             }
             */
            camera.rotate(angleX,angleY);
            //update

            // ! RENDER --------------------------------------------------------
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            this.shader.bind();


            Matrix4f view = camera.getViewMatrix();
            Matrix4f proj = new Matrix4f();
            proj = proj.perspective(90.0f, WINDOW_WIDTH / WINDOW_HEIGHT, 0.1f, 100.0f);
            this.shader.setUniform("proj", proj);
            this.shader.setUniform("view", view);

            Mesh m = geometry.get(0);
            //for(int i = 0; i < 100; i++) {
                //this.shader.setUniform("offsets[" + i + "]", translations[i]);
                glBindVertexArray(m.getVaoID());
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);

                Matrix4f model = new Matrix4f();
                //model = model.rotate(angle, axis);
                this.shader.setUniform("model", model);
                if (m.isWireframe()){
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                }

                //glDrawElementsInstanced(GL_TRIANGLES, m.getVertexCount(), GL_UNSIGNED_INT, 0, 100);
                glDrawElements(GL_TRIANGLES, m.getVertexCount(), GL_UNSIGNED_INT, 0);

                if (m.isWireframe()) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }

                glBindVertexArray(0);
            //}
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
