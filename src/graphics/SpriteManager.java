package graphics;

import javax.swing.*;
import java.awt.*;

public class SpriteManager extends JPanel {
    private static SpriteManager _instance;

    private HashMap<TextureID, Image> textureAtlas;

    private SpriteManager() {
        super();
        this.setBackground();
    }
}
