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

public class Server extends UnicastRemoteObject implements IServer {
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

        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind(SERVER_NAME, this);
            System.out.println("--- server registered ---");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        this.environment = new Environment();
        SpriteManager.instance.setEnv(this.environment);
        System.out.println("created new env");
        this.environment.randomValues(RAND_CELLS);
        this.gameOfLife = new GameOfLife(this.environment);
        this.gameOfLife.start();
        this.eventQueue.send(new InitGridEvent(), ThreadID.Render);
    }

    public void reset() {
        this.environment.purge();
        this.gameOfLife.purge();
        this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        System.gc();
    }

    @Override
    public synchronized IGOLProcess getTask() throws RemoteException {
        //System.out.println("[SERVER] dispatched task");
        this.gameOfLife.checkCompletion();
        return this.gameOfLife.getNext();
    }

    @Override
    public synchronized void sendResult(IGOLProcess t) throws RemoteException {
        //System.out.println("[SERVER] got result");
        this.gameOfLife.sendResult(t);
        this.gameOfLife.checkCompletion();
    }

    @Override
    public Status getStatus() throws RemoteException {
        return this.gameOfLife.getStatus();
    }
}
