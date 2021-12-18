package fr.albot.GameOfLife.Engine;

import fr.albot.GameOfLife.Engine.geometry.TexturedMesh;
import fr.albot.GameOfLife.core.Cell;
import fr.albot.GameOfLife.core.Environment;
import fr.albot.GameOfLife.Engine.geometry.Mesh;
import fr.albot.GameOfLife.Engine.geometry.Sprite;
import org.joml.Vector3f;

import java.util.ArrayList;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ENV_SIZE;

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


    public void purge() {
        for (Sprite s : geometry) {
            s.purge();
        }
        this.container.purge();

        this.geometry.clear();
        this.container = null;
        this.env = null;
    }

    private SpriteManager() {
        this.geometry = new ArrayList<>();
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public void init() {
        //container = new Sprite(Mesh.Cube(new Vector3f(1.0f, 1.0f, 1.0f)), true);
        //container.scale = ENV_SIZE;

        //TODO: revoir création de la grille.
        float offset = (0.5f * (ENV_SIZE - 1));
        for (int x = 0; x < ENV_SIZE; x++) {
            for (int y = 0; y < ENV_SIZE; y++) {
                for (int z = 0; z < ENV_SIZE; z++) {
                    Sprite s = new Sprite(TexturedMesh.cube(), false);
                    s.moveTo(x - offset, y - offset, z - offset);
                    s.scale = 0.8f;
                    s.hidden = true;
                    this.geometry.add(s);
                }
            }
        }

        //this.geometry.add(container);

        this.displayEnv();
    }

    public void displayEnv() {
        for (int x = 0; x < ENV_SIZE; x++) {
            for (int y = 0; y < ENV_SIZE; y++) {
                for (int z = 0; z < ENV_SIZE; z++) {
                    int index = (z * ENV_SIZE * ENV_SIZE) + (x * ENV_SIZE) + y;
                    this.geometry.get(index).hidden = (env.getCellState(x, y, z) == Cell.Empty);
                }
            }
        }
    }

    public ArrayList<Sprite> getGeometry() {
        return this.geometry;
    }
}
