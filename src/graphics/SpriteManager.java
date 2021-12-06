package graphics;

import core.Cell;
import core.Environment;
import graphics.geometry.Mesh;
import graphics.geometry.Sprite;
import org.joml.Vector3f;

import java.util.ArrayList;

import static CONFIG.CONFIG.ENV_SIZE;

public class SpriteManager {
    public static SpriteManager instance;

    private Environment env;

    private ArrayList<Sprite> geometry;
    private Sprite container;

    // le return est important => permet le lien vers le contexte OGL
    public static SpriteManager createSpriteManager() {
        if (SpriteManager.instance == null) {
            SpriteManager.instance = new SpriteManager();
        }

        return SpriteManager.instance;
    }

    private SpriteManager() {
        this.geometry = new ArrayList<>();
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public void init() {
        container = new Sprite(Mesh.Cube(new Vector3f(1.0f, 1.0f, 1.0f)), true);
        container.scale = ENV_SIZE;

        //TODO: revoir création de la grille.
        float offset = (0.5f * (ENV_SIZE - 1));
        for (int x = 0; x < ENV_SIZE; x++) {
            for (int y = 0; y < ENV_SIZE; y++) {
                //for (int k = 0; k < 5; k++) {
                Sprite s = new Sprite(Mesh.Cube(new Vector3f(0.0f, 1.0f, 0.0f)), false);
                s.moveTo(x - offset, y - offset, 0.0f);
                s.scale = 0.8f;
                s.hidden = true;
                this.geometry.add(s);
                //}
            }
        }

        this.geometry.add(container);

        this.displayEnv();
    }

    public void displayEnv() {
        for (int x = 0; x < ENV_SIZE; x++) {
            for (int y = 0; y < ENV_SIZE; y++) {
                int index = (x * ENV_SIZE) + y;
                this.geometry.get(index).hidden = (env.getCellState(x, y) == Cell.Empty);
            }
        }
    }

    public ArrayList<Sprite> getGeometry() {
        return this.geometry;
    }
}
