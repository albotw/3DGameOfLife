package graphics.engine;

import input.Keyboard;
import input.Mouse;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static CONFIG.CONFIG.FOV;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {
    private long glfwWindow;
    private int width;
    private int height;
    private final String title;
    private boolean resized;
    private boolean vSync;

    private Matrix4f projectionMatrix;

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        this.resized = false;
    }

    public void init() {
        //retour d'erreur de glfw dans la console.
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("impossible d'initialiser GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);                             //fenêtre masquée
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);                            //fenêtre redimensionnable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);                //openGL 3.2
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        glfwWindowHint(GLFW_SAMPLES, 4);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new RuntimeException("impossible de créer la fenêtre GLFW");
        }

        //pour redimensionner la fenêtre et le viewport d'opengl
        glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) ->
        {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

        Keyboard.init(this.glfwWindow);
        Mouse.init(this.glfwWindow);

        //centrage de la fenêtre
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        glfwSetWindowPos(
                glfwWindow,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );

        //activer openGL
        glfwMakeContextCurrent(glfwWindow);

        if (isvSync()) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(glfwWindow);
        GLFW.glfwMakeContextCurrent(glfwWindow);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_MULTISAMPLE);

        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.perspective(FOV, this.width / this.height, 0.01f, 100.0f);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(glfwWindow);
    }

    public boolean isResized() {
        return resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void update() {
        if (isResized()) {
            glViewport(0, 0, width, height);
            setResized(false);
        }

        glfwSwapBuffers(glfwWindow);
        glfwPollEvents();
    }

}
