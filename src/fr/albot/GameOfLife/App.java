package fr.albot.GameOfLife;

import fr.albot.GameOfLife.Engine.GL.Renderer;
import fr.albot.GameOfLife.Engine.events.EventDispatcher;
import fr.albot.GameOfLife.Engine.events.EventQueue;
import fr.albot.GameOfLife.Engine.events.Events.PurgeEvent;
import fr.albot.GameOfLife.Engine.events.ThreadID;
import fr.albot.GameOfLife.core.GameOfLife;

import static fr.albot.GameOfLife.CONFIG.CONFIG.WAIT_DELAY;

public class App extends Thread {
    private Renderer renderer;
    private EventQueue eventQueue;

    public GameOfLife gol;

    public boolean loop = false;
    private boolean exit = false;

    public static App instance;

    public static void main(String[] args) {
        App.instance = new App();
    }

    public App() {
        EventDispatcher.createEventDispatcher();
        this.eventQueue = new EventQueue(ThreadID.App);
        this.renderer = new Renderer();
        this.renderer.start();

        this.init();
        this.start();
    }

    public void init() {
        System.gc();
        this.gol = new GameOfLife();
        this.gol.init();
    }

    public void purge() {
        this.gol.purge();
        this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        System.out.println("send event");
    }

    public void run() {
        while (!exit) {
            if (loop) {
                this.gol.step();
            }
            try {
                sleep(WAIT_DELAY);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void exit() {
        this.loop = false;
        this.exit = true;
        this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        this.gol.purge();
        System.exit(0);
    }
}
