package fr.albot.GameOfLife;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.Engine.GL.Renderer;
import fr.albot.GameOfLife.Engine.SpriteManager;
import fr.albot.GameOfLife.Engine.events.Event;
import fr.albot.GameOfLife.Engine.events.EventDispatcher;
import fr.albot.GameOfLife.Engine.events.EventQueue;
import fr.albot.GameOfLife.Engine.events.Events.InitGridEvent;
import fr.albot.GameOfLife.Engine.events.Events.PurgeEvent;
import fr.albot.GameOfLife.Engine.events.Events.SpriteUpdateDoneEvent;
import fr.albot.GameOfLife.Engine.events.ThreadID;
import fr.albot.GameOfLife.core.Environment;
import fr.albot.GameOfLife.core.GameOfLife;
import fr.albot.GameOfLife.core.Status;

import static fr.albot.GameOfLife.CONFIG.CONFIG.RAND_CELLS;

public class Server extends Thread {
    private GameOfLife gameOfLife;
    private Renderer renderer;
    private EventQueue eventQueue;
    private Environment environment;

    public static Server instance;
    private boolean running = true;

    public static void main(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-nodisplay":
                        CONFIG.RENDER_ACTIVE = false;
                        System.out.println("Disabled rendering");
                        break;
                    case "-envsize":
                        CONFIG.ENV_SIZE = Integer.parseInt(args[i + 1]);
                        RAND_CELLS = (CONFIG.ENV_SIZE * CONFIG.ENV_SIZE * CONFIG.ENV_SIZE) / 2;
                        System.out.println("Set env_size to " + CONFIG.ENV_SIZE);
                        break;
                    case "-cells":
                        RAND_CELLS = Integer.parseInt(args[i + 1]);
                        System.out.println("Will generate " + RAND_CELLS + " cells at random positions");
                        break;
                    case "-alive":
                        CONFIG.ALIVE_THRESHOLD = Integer.parseInt(args[i + 1]);
                        System.out.println(CONFIG.ALIVE_THRESHOLD + " alive neighbours for a cell to be alive");
                        break;
                    case "-current":
                        CONFIG.CURRENT_THRESHOLD = Integer.parseInt(args[i + 1]);
                        System.out.println(CONFIG.CURRENT_THRESHOLD + " alive neighbours to keep current state");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        Server srv = new Server();
        System.out.println(RAND_CELLS);
        if (CONFIG.RENDER_ACTIVE) {
            srv.start();
        }
    }

    public Server() {
        Server.instance = this;

        EventDispatcher.createEventDispatcher();
        this.eventQueue = new EventQueue(ThreadID.Server);

        if (CONFIG.RENDER_ACTIVE) {
            this.renderer = new Renderer();
            this.renderer.start();
        }

        this.init();
    }

    public void run() {
        while (running) {
            if (!this.eventQueue.isEmpty()) {
                Event e = this.eventQueue.get();
                if (e instanceof SpriteUpdateDoneEvent) {
                    this.gameOfLife.setStatus(Status.CONTINUE);
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

        if (CONFIG.RENDER_ACTIVE) {
            SpriteManager.instance.setEnv(this.environment);
        }

        this.environment.randomValues(RAND_CELLS);

        try {
            this.gameOfLife = new GameOfLife(this.environment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (CONFIG.RENDER_ACTIVE) {
            this.eventQueue.send(new InitGridEvent(), ThreadID.Render);
        } else {
            this.gameOfLife.init();
        }
    }

    public void reset() {
        this.environment.purge();
        this.gameOfLife.purge();
        if (CONFIG.RENDER_ACTIVE) {
            this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        }
        System.gc();
    }

    public void activate() {
        this.gameOfLife.init();
    }
}
