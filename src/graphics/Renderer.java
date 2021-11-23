package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static CONFIG.CONFIG.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer extends Thread{
    private Window window;
    private Shader shader;
    private ArrayList<Mesh> geometry;

    public Renderer() {
        this.window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, VSYNC);
        this.geometry = new ArrayList<>();
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
        this.shader.createUniform("model");
        this.shader.createUniform("view");
        this.shader.createUniform("proj");
        System.out.println("finished render init");
    }

    public void renderMesh(Mesh m) {
        this.geometry.add(m);
    }

    public void render() throws Exception {
        boolean running = true;
        System.out.println("started rendering");
        float angle = 0;
        while(running && !window.windowShouldClose()) {
            //input
            //update

            // ! RENDER --------------------------------------------------------
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            this.shader.bind();

            Matrix4f view = new Matrix4f();
            Matrix4f proj = new Matrix4f();
            proj = proj.perspective(45.0f, WINDOW_WIDTH / WINDOW_HEIGHT, 0.1f, 100.0f);
            view = view.translation(0.0f, 0.0f, -3.0f);
            this.shader.setUniform("proj", proj);
            this.shader.setUniform("view", view);

            for(Mesh m : this.geometry) {
                glBindVertexArray(m.getVaoID());
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);

                angle += Math.sin((float)System.currentTimeMillis()) / 100;
                System.out.println(angle);
                Matrix4f model = new Matrix4f().rotate(angle, new Vector3f(1.0f, 1.0f, 1.0f));
                this.shader.setUniform("model", model);
                if (m.isWireframe()){
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                }

                glDrawElements(GL_TRIANGLES, m.getVertexCount(), GL_UNSIGNED_INT, 0);

                if (m.isWireframe()) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }

                glBindVertexArray(0);
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
