package input;

import graphics.UI.UI;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    public static boolean UP_press;
    public static boolean DOWN_press;
    public static boolean LEFT_press;
    public static boolean RIGHT_press;
    public static boolean Z_press;
    public static boolean S_press;

    public static boolean ZERO_press;

    public static void init(long window) {
        glfwSetKeyCallback(window, Keyboard::processKeyboardEvents);
        glfwSetCharCallback(window, UI.instance::processChar);
    }

    public static void processKeyboardEvents(long window, int key, int scancode, int action, int mods) {
        UI.instance.processKey(window, key, scancode, action, mods);
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(window, true);
        }
        switch (key) {
            case GLFW_KEY_UP:
                UP_press = (action == GLFW_PRESS);
                break;
            case GLFW_KEY_DOWN:
                DOWN_press = (action == GLFW_PRESS);
                break;
            case GLFW_KEY_LEFT:
                LEFT_press = (action == GLFW_PRESS);
                break;
            case GLFW_KEY_RIGHT:
                RIGHT_press = (action == GLFW_PRESS);
                break;
            case GLFW_KEY_0:
                ZERO_press = (action == GLFW_PRESS);
                break;
            case GLFW_KEY_Z:
                Z_press = (action == GLFW_PRESS);
                break;
            case GLFW_KEY_S:
                S_press = (action == GLFW_PRESS);
                break;
        }
    }
}
