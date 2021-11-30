import core.Cell;
import core.Environment;
import graphics.Mesh;
import graphics.Renderer;

import static CONFIG.CONFIG.ENV_SIZE;

public class App {
    public static Environment env;
    public static void main(String[] args)
    {
        Renderer r = new Renderer();
        r.start();
    }

}
