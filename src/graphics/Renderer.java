package graphics;

import java.awt.*;

public class Renderer extends Thread{
    private boolean running;
    private Window w;
    private SpriteManager spr_mgr;

    public Renderer() {
        this.w = new Window(800, 600);
        this.sm = SpriteManager.createSpriteManager();

        this.w.setLayout(new BorderLayout());
        this.w.add(this.spr_mgr, BorderLayout.CENTER);

        this.running = true;

        this.w.revalidate();
    }

    public void run() {
        System.out.println("--- Renderer started ---");

        while(!running) {
            w.repaint();

            try {
                sleep(16);
            }catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("--- Renderer stopped ---");
        this.spr_mgr.cleanSprites();
        this.w.setVisible(false);
        this.w.dispose();

        this.w = null;
        this.spr_mgr = null;
    }
}
