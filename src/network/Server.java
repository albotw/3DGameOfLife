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
    //TODO: voir structure de l'application.
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
        this.eventQueue = new EventQueue(ThreadID.Server);
        this.environment = new Environment();
        this.gameOfLife = new GameOfLife(this.environment);
        this.renderer = new Renderer();
        this.renderer.start();
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
        System.out.println("[SERVER] dispatched task");
        this.gameOfLife.checkCompletion();
        return this.gameOfLife.getNext();
    }

    @Override
    public synchronized void sendResult(IGOLProcess t) throws RemoteException {
        System.out.println("[SERVER] got result");
        this.gameOfLife.sendResult(t);
        this.gameOfLife.checkCompletion();
        SpriteManager.instance.displayEnv(this.environment);
    }

    @Override
    public Status getStatus() throws RemoteException {
        return this.gameOfLife.getStatus();
    }
}
