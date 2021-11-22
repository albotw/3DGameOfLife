package graphics;

import java.util.ArrayList;

import static CONFIG.CONFIG.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
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
        shader.createFragmentShader(Util.loadResource("shaders/fragment.glsl"));
        shader.createVertexShader(Util.loadResource("shaders/vertex.glsl"));
        shader.link();
        System.out.println("finished render init");
    }

    public void renderMesh(Mesh m) {
        this.geometry.add(m);
    }

    public void render() throws Exception {
        boolean running = true;
        System.out.println("started rendering");
        while(running && !window.windowShouldClose()) {
            //input
            //update

            // ! RENDER --------------------------------------------------------
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            this.shader.bind();
            for(Mesh m : this.geometry) {
                glBindVertexArray(m.getVaoID());
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);
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
