package fr.albot.GameOfLife.Engine.UI;

import fr.albot.GameOfLife.Server;
import org.lwjgl.nuklear.NkRect;

import static org.lwjgl.nuklear.Nuklear.*;

public class UILayout {
    private NkRect panel = null;
    private int panelOptions = NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE;

    public UILayout() {
    }

    public void layout(int x, int y) {
        if (this.panel == null) {
            this.panel = NkRect.create();
            nk_rect(x, y, 300, 200, this.panel);
            nk_begin(UI.context, "Commandes", this.panel, this.panelOptions);
            nk_end(UI.context);
        } else {
            if (nk_begin(UI.context, "Commandes", this.panel, panelOptions)) {
                nk_layout_row_dynamic(UI.context, 30, 3);
                if (nk_button_label(UI.context, "Start")) {
                    Server.instance.activate();
                }
                if (nk_button_label(UI.context, "Reset")) {
                    Server.instance.reset();
                }
            }
            nk_end(UI.context);
        }
    }
}
