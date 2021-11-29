package input;

import static CONFIG.CONFIG.MOUSE_SENSITIVITY;
import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public static double Xoffset;
    public static double Yoffset;

    public static double lastX = 400;
    public static double lastY = 300;

    public static void init(long window) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPosCallback(window, Mouse::processMousePosition);

        glfwSetMouseButtonCallback(window, Mouse::processMouseInput);

        glfwSetScrollCallback(window, Mouse::processMouseScroll);

        System.out.println("done init mouse");
    }

    public static void processMousePosition(long window, double xpos, double ypos) {
        Mouse.Xoffset = (Mouse.lastX - xpos) * MOUSE_SENSITIVITY;
        Mouse.Yoffset = (Mouse.lastY - ypos) * MOUSE_SENSITIVITY;

        Mouse.lastX = xpos;
        Mouse.lastY = ypos;
    }

    public static void processMouseInput(long window, int button, int action, int mods) {

    }

    public static void processMouseScroll(long window, double xoffset, double yoffset) {

    }
}
