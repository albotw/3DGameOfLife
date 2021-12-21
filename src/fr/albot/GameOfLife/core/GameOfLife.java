package fr.albot.GameOfLife.core;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.Engine.SpriteManager;

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

    public GameOfLife(Environment env) throws RemoteException {
        super();

        this.step = 1;

        this.toDelete = new ArrayList<Integer>();
        this.toCreate = new ArrayList<Integer>();

        this.lastIndex = new AtomicInteger(0);

        this.env = new Environment();
        this.env.blinker();
    }

    public void init() {
        //this.createChunks();
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
        //this.env = null;
    }


    public void checkCompletion() {
        if (this.lastIndex.get() > this.env.getAliveSize() && step == 1) {
            this.status = Status.WAIT;
            step = 2;
            this.lastIndex.set(0);
            this.status = Status.CONTINUE;
        } else if (this.lastIndex.get() > this.env.getNeighboursSize() && step == 2) {
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

    public IGOLProcess getNext() throws RemoteException {
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

    public void sendResult(IGOLProcess t) throws RemoteException {
        if (step == 1) {
            this.toDelete.addAll(t.getResult());
            //System.out.println(Arrays.toString(this.toDelete.toArray()));
        }
        if (step == 2) {
            this.toCreate.addAll(t.getResult());
            //System.out.println(Arrays.toString(this.toCreate.toArray()));
            // t.getNextNeighbours().forEach((key, value) -> this.next_neighbours.merge(key, value, (v1, v2) -> Objects.equals(v1, v2) ? v1 : v1 + v2));
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
