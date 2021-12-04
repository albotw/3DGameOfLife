package graphics;

import core.Environment;

import java.util.ArrayList;

public class SpriteManager {
    public static SpriteManager instance;

    private ArrayList<Sprite> geometry;

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

    public void init() {
        Sprite container = new Sprite(Mesh.Cube(), true);
        container.scale = 5;
        this.geometry.add(container);

        float offset = 2f;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    Sprite s = new Sprite(Mesh.Cube(), false);
                    s.moveTo(i - offset, j - offset, k - offset);
                    s.scale = 0.8f;
                    this.geometry.add(s);
                }
            }
        }
    }

    public void update(Environment env) {

    }

    public ArrayList<Sprite> getGeometry() {
        return this.geometry;
    }
}
