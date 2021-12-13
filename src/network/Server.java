package network;

import core.Environment;
import core.GameOfLife;
import core.IGOLProcess;
import core.Status;
import events.EventDispatcher;
import events.EventQueue;
import events.Events.InitGridEvent;
import events.Events.PurgeEvent;
import events.ThreadID;
import Engine.SpriteManager;
import Engine.GL.Renderer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static CONFIG.CONFIG.RAND_CELLS;
import static CONFIG.CONFIG.SERVER_NAME;

public class Server {
    private GameOfLife gameOfLife;
    private Renderer renderer;
    private EventQueue eventQueue;
    private Environment environment;

    public static Server instance;

    public static void main(String[] args) {
        try {
            Server srv = new Server();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Server() throws RemoteException {
        super();
        Server.instance = this;

        EventDispatcher.createEventDispatcher();

        this.renderer = new Renderer();
        this.renderer.start();
        this.eventQueue = new EventQueue(ThreadID.Server);

        this.init();
    }

    public void init() {
        this.environment = new Environment();
        SpriteManager.instance.setEnv(this.environment);
        System.out.println("created new env");
        this.environment.randomValues(RAND_CELLS);

        try {
            this.gameOfLife = new GameOfLife(this.environment);
        }catch(Exception ex) {ex.printStackTrace();}

        this.eventQueue.send(new InitGridEvent(), ThreadID.Render);
    }

    public void reset() {
        this.environment.purge();
        this.gameOfLife.purge();
        this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        System.gc();
    }
}
