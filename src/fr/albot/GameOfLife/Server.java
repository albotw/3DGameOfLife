package fr.albot.GameOfLife;

import fr.albot.GameOfLife.Engine.GL.Renderer;
import fr.albot.GameOfLife.Engine.SpriteManager;
import fr.albot.GameOfLife.core.Environment;
import fr.albot.GameOfLife.core.GameOfLife;
import fr.albot.GameOfLife.core.Status;
import fr.albot.GameOfLife.events.Event;
import fr.albot.GameOfLife.events.EventDispatcher;
import fr.albot.GameOfLife.events.EventQueue;
import fr.albot.GameOfLife.events.Events.InitGridEvent;
import fr.albot.GameOfLife.events.Events.PurgeEvent;
import fr.albot.GameOfLife.events.Events.SpriteUpdateDoneEvent;
import fr.albot.GameOfLife.events.ThreadID;

import static fr.albot.GameOfLife.CONFIG.CONFIG.RAND_CELLS;

public class Server extends Thread {
    private GameOfLife gameOfLife;
    private Renderer renderer;
    private EventQueue eventQueue;
    private Environment environment;

    public static Server instance;
    private boolean running = true;

    public static void main(String[] args) {
        Server srv = new Server();
        srv.start();
    }

    public Server() {
        Server.instance = this;

        EventDispatcher.createEventDispatcher();

        this.renderer = new Renderer();
        this.renderer.start();
        this.eventQueue = new EventQueue(ThreadID.Server);

        this.init();
    }

    public void run() {
        while (running) {
            if (!this.eventQueue.isEmpty()) {
                Event e = this.eventQueue.get();
                if (e instanceof SpriteUpdateDoneEvent) {
                    this.gameOfLife.setStatus(Status.CONTINUE);
                    System.out.println("CONTINUE");
                }
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void init() {
        this.environment = new Environment();
        SpriteManager.instance.setEnv(this.environment);
        System.out.println("created new env");
        this.environment.randomValues(RAND_CELLS);

        try {
            this.gameOfLife = new GameOfLife(this.environment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.eventQueue.send(new InitGridEvent(), ThreadID.Render);
    }

    public void reset() {
        this.environment.purge();
        this.gameOfLife.purge();
        this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        System.gc();
    }

    public void activate() {
        this.gameOfLife.init();
    }
}
