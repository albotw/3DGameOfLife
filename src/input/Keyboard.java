package input;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
    public static void init(long window) {
        glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            Keyboard.processKeyboardEvents(w, key, scancode, action, mods);
        });
    }

    public static void processKeyboardEvents(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(window,true);
        }
    }
}
