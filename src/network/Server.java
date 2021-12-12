package network;

import core.v2.Cell;
import core.v2.GameOfLife;
import events.EventDispatcher;
import events.EventQueue;
import events.Events.PurgeEvent;
import events.ThreadID;
import engine.GL.Renderer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static CONFIG.CONFIG.SERVER_NAME;

public class Server extends UnicastRemoteObject implements IServer {
    private GameOfLife gameOfLife;
    private Renderer renderer;
    private EventQueue eventQueue;

    private long before = 0;
    private int alivePing = 0;
    private int inCalls = 0;
    private int outCalls = 0;

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

        //this.renderer = new Renderer();
        //this.renderer.start();
        this.eventQueue = new EventQueue(ThreadID.Server);

        this.init();
        this.gameOfLife.setStatus(Status.CONTINUE);
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind(SERVER_NAME, this);
            System.out.println("--- server registered ---");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        //SpriteManager.instance.setEnv(this.environment);
        System.out.println("created new env");
        //this.environment.randomValues(50);
        this.gameOfLife = new GameOfLife();
        this.gameOfLife.randomValues(50);
        //this.eventQueue.send(new InitGridEvent(), ThreadID.Render);
    }

    public void reset() {
        //this.environment.purge();
        //this.gameOfLife.purge();
        this.eventQueue.send(new PurgeEvent(), ThreadID.Render);
        System.gc();
    }

    public void finishCycle() {
        long after = System.currentTimeMillis();
        System.out.println("Cycle done in " + (after - before) + "ms");
        System.out.println("in calls: " + this.inCalls + " out calls: " + this.outCalls + " pings: " + this.alivePing);
        this.gameOfLife.checkCompletion();

        this.before = System.currentTimeMillis();
        this.inCalls = 0;
        this.outCalls = 0;
        this.alivePing = 0;
    }

    @Override
    public synchronized Cell getTask() throws RemoteException {
        if (before == 0) {before = System.currentTimeMillis();}
        this.outCalls++;
        return this.gameOfLife.getNext();
    }

    @Override
    public synchronized void sendResult(Cell c) throws RemoteException {
        this.inCalls++;
        this.gameOfLife.setCell(c);
    }

    @Override
    //public List<Boolean> isAlive(ArrayList<Cell> cells) throws RemoteException {
    public boolean isAlive(Cell c) throws RemoteException {
        this.alivePing++;
        return this.gameOfLife.isAlive(c);
        //return cells.stream().map(c -> this.gameOfLife.isAlive(c)).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Status getStatus() throws RemoteException {
        return this.gameOfLife.getStatus();
    }
}
