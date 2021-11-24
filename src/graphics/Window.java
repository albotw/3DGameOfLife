package graphics;

import input.Keyboard;
import input.Mouse;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long glfwWindow;
    private int width;
    private int height;
    private String title;
    private boolean resized;
    private boolean vSync;

    public static final float FOV = (float) Math.toRadians(60.0f);

    public static final float Z_NEAR = 0.01f;

    public static final float Z_FAR = 1000.0f;

    private Matrix4f projectionMatrix;

    public Window(int width, int height, String title, boolean vSync)
    {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        this.resized = false;
    }

    public void init()
    {
        //retour d'erreur de glfw dans la console.
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
        {
            throw new IllegalStateException("impossible d'initialiser GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);                             //fenêtre masquée
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);                            //fenêtre redimensionnable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);                //openGL 3.2
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL)
        {
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
        glfwSetWindowPos(
                glfwWindow,
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
        );

        //activer openGL
        glfwMakeContextCurrent(glfwWindow);

        if (isvSync())
        {
            glfwSwapInterval(1);
        }

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    public boolean windowShouldClose()
    {
        return glfwWindowShouldClose(glfwWindow);
    }

    public void setClearColor(float r, float g, float b, float alpha)
    {
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode)
    {
        return glfwGetKey(glfwWindow, keyCode) == GLFW_PRESS;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public boolean isResized()
    {
        return resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setResized(boolean resized)
    {
        this.resized = resized;
    }

    public void setvSync(boolean vSync)
    {
        this.vSync = vSync;
    }

    public void update()
    {
        if (isResized())
        {
            glViewport(0, 0, width, height);
            setResized(false);
        }

        glfwSwapBuffers(glfwWindow);
        glfwPollEvents();
    }

}
