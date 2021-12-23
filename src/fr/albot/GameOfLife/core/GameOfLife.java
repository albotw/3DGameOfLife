package fr.albot.GameOfLife.core;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.Engine.SpriteManager;
import fr.albot.GameOfLife.Engine.events.EventDispatcher;
import fr.albot.GameOfLife.Engine.events.Events.SetBaseEnv;
import fr.albot.GameOfLife.Engine.events.ThreadID;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static fr.albot.GameOfLife.CONFIG.CONFIG.CHUNK_SIZE;
import static fr.albot.GameOfLife.CONFIG.CONFIG.SERVER_NAME;

public class GameOfLife extends UnicastRemoteObject implements IGameOfLife {
    private long before_generation = 0;
    private Status status = Status.CONTINUE;
    private ArrayList<Integer> toDelete;
    private ArrayList<Integer> toCreate;
    private AtomicInteger lastIndex;

    private Environment env;

    private int step; //1: alive, 2: neighbours.

    public GameOfLife() throws RemoteException {
        super();

        this.step = 1;

        this.toDelete = new ArrayList<Integer>();
        this.toCreate = new ArrayList<Integer>();

        this.lastIndex = new AtomicInteger(0);

        this.env = new Environment();
    }

    public void init() {
        if (CONFIG.PATTERN == Pattern.CUSTOM) {
            this.env.loadFromFile(CONFIG.PATTERN_FILE);
        } else if (CONFIG.PATTERN == Pattern.RAND) {
            this.env.randomValues();
        }

        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Naming.rebind(SERVER_NAME, this);
            System.out.println("--- server registered ---");
        } catch (MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }

        if (CONFIG.RENDER_ACTIVE) {
            EventDispatcher.instance.publish(new SetBaseEnv(this.env.getAlive()), ThreadID.Render);
        }

        this.status = Status.CONTINUE;
    }

    public void purge() {
        this.status = Status.WAIT;
        this.before_generation = 0;
        this.toCreate.clear();
        this.toDelete.clear();
        this.env.purge();
    }


    public void checkCompletion() {
        if (this.lastIndex.get() >= this.env.getAliveSize() && step == 1) {
            this.status = Status.WAIT;
            step = 2;
            this.lastIndex.set(0);
            this.status = Status.CONTINUE;
        } else if (this.lastIndex.get() >= this.env.getNeighboursSize() && step == 2) {
            this.status = Status.WAIT;

            this.env.nextGeneration(toDelete, toCreate);

            this.toDelete.clear();
            this.toCreate.clear();

            if (CONFIG.RENDER_ACTIVE) {
                SpriteManager.instance.update(this.env.getAlive());
            }

            System.out.println("--- Generation completed in " + (System.currentTimeMillis() - before_generation) + " ms ---");
            this.before_generation = 0;

            step = 1;
            this.lastIndex.set(0);
            this.status = Status.CONTINUE;
        }
    }

    public synchronized IGOLProcess getNext() throws RemoteException {
        if (before_generation == 0) {
            before_generation = System.currentTimeMillis();
        }
        this.checkCompletion();
        if (this.step == 1) {
            return new PurgeProcess(this.env.getAliveSubset(lastIndex.getAndAdd(CHUNK_SIZE)), this.env.getNeighbours());
        } else if (this.step == 2) {
            return new GenerateProcess(this.env.getAlive(), this.env.getNeighboursSubset(lastIndex.getAndAdd(CHUNK_SIZE)));
        } else {
            System.out.println("null task ??");
            return null;
        }
    }

    public synchronized void sendResult(IGOLProcess t) throws RemoteException {
        if (step == 1) {
            this.toDelete.addAll(t.getResult());
        }
        if (step == 2) {
            this.toCreate.addAll(t.getResult());
        }
    }

    @Override
    public Status getStatus() throws RemoteException {
        return this.status;
    }
}
