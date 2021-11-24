package input;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public static void init(long window) {
        glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            Mouse.processMousePosition(w, xpos, ypos);
        });

        glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            Mouse.processMouseInput(w, button, action, mods);
        });

        glfwSetScrollCallback(window, (w, xoffset, yoffset) -> {
            Mouse.processMouseScroll(w, xoffset, yoffset);
        });
    }

    public static void processMousePosition(long window, double xpos, double ypos) {

    }

    public static void processMouseInput(long window, int button, int action, int mods) {

    }

    public static void processMouseScroll(long window, double xoffset, double yoffset) {

    }
}
