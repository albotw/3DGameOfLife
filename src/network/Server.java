package network;

import core.Environment;
import core.GameOfLife;
import core.IGOLProcess;
import events.EventDispatcher;
import events.EventQueue;
import events.ThreadID;
import graphics.engine.Renderer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements IServer {
    //TODO: voir structure de l'application.
    private GameOfLife gameOfLife;
    private Renderer renderer;
    private EventQueue eventQueue;
    private Environment environment;

    private Status status;

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
        this.status = Status.WAIT;
        Server.instance = this;

        EventDispatcher.createEventDispatcher();
        this.eventQueue = new EventQueue(ThreadID.Server);
        this.environment = new Environment();
        this.gameOfLife = new GameOfLife(this.environment);
        this.renderer = new Renderer();
        this.renderer.start();
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind("GOL_SERVER", this);
            System.out.println("--- server registered ---");
            this.status = Status.CONTINUE;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized IGOLProcess getTask() throws RemoteException {
        System.out.println("[SERVER] dispatched task");
        if (this.status == Status.WAIT) {
            this.environment.nextGeneration();
            this.status = Status.CONTINUE;
        }
        return this.gameOfLife.getNext();
    }

    @Override
    public synchronized void sendResult(IGOLProcess t) throws RemoteException {
        System.out.println("[SERVER] got result");
        this.gameOfLife.sendResult(t);
    }

    @Override
    public Status getStatus() throws RemoteException {
        return this.status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }
}
