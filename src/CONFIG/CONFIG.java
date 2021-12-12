package CONFIG;

public class CONFIG {
    //configuration rendu
    public static int RENDER_TICK = 16;
    public static String WINDOW_TITLE = "Game Of Life";
    public static int WINDOW_HEIGHT = 1024;
    public static int WINDOW_WIDTH = 1024;
    public static boolean VSYNC = true;
    public static float MOUSE_SENSITIVITY = 0.01f;
    public static float FOV = 45.0f;
    public static float EPSILON = 0.00001f;

    //configuration process
    public static int ENV_SIZE = 100;
    public static int SUB_ENV_SIZE = 3;                        // ! IMPAIR OBLIGATOIRE
    public static String SERVER_NAME = "GOL_SERVER";
    public static int ALIVE_THRESOLD = 3;
    public static int CURRENT_THRESHOLD = 2;
}
