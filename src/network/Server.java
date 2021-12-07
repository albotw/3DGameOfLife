package network;

import core.Environment;
import core.GameOfLife;
import core.IGOLProcess;
import events.EventDispatcher;
import events.EventQueue;
import events.ThreadID;
import graphics.SpriteManager;
import graphics.engine.Renderer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
        this.environment = new Environment();

        //TODO: chargement des valeurs depuis GRID.txt
        int[] initPositions = {
                0, 1,
                1, 1,
                2, 1
        };
        //this.environment.initValues(initPositions);
        this.environment.randomValues(20);


        this.gameOfLife = new GameOfLife(this.environment);

        SpriteManager.instance.setEnv(this.environment);

        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind(SERVER_NAME, this);
            System.out.println("--- server registered ---");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
