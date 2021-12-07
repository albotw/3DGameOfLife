package input;

import graphics.UI.UI;

import static CONFIG.CONFIG.MOUSE_SENSITIVITY;
import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    public static double Xoffset;
    public static double Yoffset;

    public static double lastX = 400;
    public static double lastY = 300;

    public static boolean LMBPress;
    public static boolean RMBPress;

    public static void init(long window) {
        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        glfwSetCursorPosCallback(window, Mouse::processMousePosition);

        glfwSetMouseButtonCallback(window, Mouse::processMouseInput);

        glfwSetScrollCallback(window, UI.instance::processScroll);
    }

    public static void processMousePosition(long window, double xpos, double ypos) {
        UI.instance.processCursor(window, xpos, ypos);
        Mouse.Xoffset = (Mouse.lastX - xpos) * MOUSE_SENSITIVITY;
        Mouse.Yoffset = (Mouse.lastY - ypos) * MOUSE_SENSITIVITY;

        Mouse.lastX = xpos;
        Mouse.lastY = ypos;
    }

    public static void processMouseInput(long window, int button, int action, int mods) {
        UI.instance.processMouseButtons(window, button, action, mods);
        switch (button) {
            case GLFW_MOUSE_BUTTON_RIGHT:
                Mouse.RMBPress = (action == GLFW_PRESS);
                break;
            case GLFW_MOUSE_BUTTON_LEFT:
                Mouse.LMBPress = (action == GLFW_PRESS);
                break;
        }
    }
}
