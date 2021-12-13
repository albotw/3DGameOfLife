package core;

import events.Event;
import events.EventDispatcher;
import events.EventQueue;
import events.Events.KillEvent;
import events.Events.SpriteUpdateDoneEvent;
import events.Events.UpdateSpritesEvent;
import events.ThreadID;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static CONFIG.CONFIG.ENV_SIZE;
import static CONFIG.CONFIG.SERVER_NAME;

public class GameOfLife extends UnicastRemoteObject implements IGameOfLife {
    public Environment env;
    private EventQueue eventQueue;
    private int x;
    private int y;
    private int z;
    public Status status;

    public GameOfLife(Environment env) throws RemoteException {
        super();
        this.eventQueue = new EventQueue(ThreadID.App);
        this.env = env;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void init() {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind(SERVER_NAME, this);
            System.out.println("--- server registered ---");
        } catch (MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        this.status = Status.CONTINUE;
    }

    public void purge() {
        this.status = Status.WAIT;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.env = null;
    }

    public void checkCompletion() {
        if (this.status == Status.WAIT) {
            this.env.nextGeneration();
            EventDispatcher.instance.publish(new UpdateSpritesEvent(), ThreadID.Render);

            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
    }

    @Override
    public synchronized IGOLProcess getNext() throws RemoteException {
        if (y < ENV_SIZE && x < ENV_SIZE && z < ENV_SIZE) {
            Cell[][][] local_env = env.getSubEnv(x, y, z);
            GOLProcess task = new GOLProcess(x, y, z, local_env);
            y++;
            return task;
        } else {
            y = 0;
            if (x + 1 < ENV_SIZE) x++;
            else {
                x = 0;
                if (z + 1 < ENV_SIZE) z++;
                else {
                    System.out.println("WAIT");
                    this.status = Status.WAIT;
                    this.checkCompletion();
                }
            }

            return null;
        }
    }

    @Override
    public synchronized void sendResult(IGOLProcess t) throws RemoteException{
        if (t != null) {
            GOLProcess task = (GOLProcess) t;
            env.setCellState(task.x, task.y, task.z, task.updatedState());
        }
    }

    @Override
    public Status getStatus() throws RemoteException {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
