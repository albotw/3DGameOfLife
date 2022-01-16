package fr.albot.GameOfLife.CONFIG;

import fr.albot.GameOfLife.core.Pattern;

public class CONFIG {
    //configuration rendu
    public static boolean INSTANCED_RENDER = true;
    public static String WINDOW_TITLE = "Game Of Life";
    public static int WINDOW_HEIGHT = 1024;
    public static int WINDOW_WIDTH = 1024;
    public static boolean VSYNC = true;
    public static float MOUSE_SENSITIVITY = 0.01f;
    public static float FOV = 45.0f;
    public static float EPSILON = 0.00001f;

    //configuration process
    public static boolean RENDER_ACTIVE = true;
    public static Pattern PATTERN = Pattern.RAND;
    public static String PATTERN_FILE = "";
    public static int ENV_SIZE = 10;
    public static int ENV_LENGTH = ENV_SIZE * ENV_SIZE * ENV_SIZE;
    public static int CHUNK_SIZE = 10;
    public static int RAND_CELLS = ENV_LENGTH / 2;
    public static String SERVER_NAME = "GOL_SERVER";
    public static int ALIVE_THRESHOLD = 5;
    public static int CURRENT_THRESHOLD = 4;
    public static int WAIT_DELAY = 500;

    public static void setEnvSize(int value) {
        CONFIG.ENV_SIZE = value;
        CONFIG.ENV_LENGTH = ENV_SIZE * ENV_SIZE * ENV_SIZE;
        CONFIG.RAND_CELLS = ENV_LENGTH / 2;
    }

    public static void setRandCells(int value) {
        if (value < ENV_LENGTH && value >= 0) {
            CONFIG.RAND_CELLS = value;
        }
    }
}
