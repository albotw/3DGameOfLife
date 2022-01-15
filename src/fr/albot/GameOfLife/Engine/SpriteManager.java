package fr.albot.GameOfLife.Engine;

import fr.albot.GameOfLife.Engine.geometry.Sprite;
import fr.albot.GameOfLife.Engine.geometry.TexturedMesh;

import java.util.ArrayList;
import java.util.HashSet;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ENV_SIZE;

public class SpriteManager {
    public static SpriteManager instance;

    private ArrayList<Sprite> geometry;
    private int envsize;

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

        this.geometry.clear();
    }

    private SpriteManager() {
        this.geometry = new ArrayList<>();
    }

    public void init() {
        this.envsize = ENV_SIZE;
        //TODO: revoir création de la grille.
        float offset = (0.5f * (this.envsize - 1));
        for (int x = 0; x < this.envsize; x++) {
            for (int y = 0; y < this.envsize; y++) {
                for (int z = 0; z < this.envsize; z++) {
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
