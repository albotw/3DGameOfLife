package fr.albot.GameOfLife.Engine;

import fr.albot.GameOfLife.Engine.geometry.Sprite;
import fr.albot.GameOfLife.Engine.geometry.TexturedMesh;

import java.util.ArrayList;
import java.util.HashSet;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ENV_SIZE;

public class SpriteManager {
    public static SpriteManager instance;

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
    }

    private SpriteManager() {
        this.geometry = new ArrayList<>();
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
    }

    public void update(HashSet<Integer> alive) {
        for (Sprite s : this.geometry) {
            if (alive.contains(this.geometry.indexOf(s))) {
                s.hidden = false;
            } else s.hidden = true;
        }
    }

    public ArrayList<Sprite> getGeometry() {
        return this.geometry;
    }
}
