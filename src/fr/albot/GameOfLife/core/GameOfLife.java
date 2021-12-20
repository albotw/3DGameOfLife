package fr.albot.GameOfLife.core;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.Engine.events.EventDispatcher;
import fr.albot.GameOfLife.Engine.events.Events.UpdateSpritesEvent;
import fr.albot.GameOfLife.Engine.events.ThreadID;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;

public class GameOfLife extends UnicastRemoteObject implements IGameOfLife {
    public Environment env;
    private AtomicInteger pos;
    public Status status;

    private long before_generation = 0;

    //v3
    private HashSet<Integer> alive;
    private HashMap<Integer, Integer> neighbours; // position, nombre de voisins.
    private HashSet<Integer> next_alive;
    private HashMap<Integer, Integer> next_neighbours;
    private AtomicInteger lastIndex;

    private int step; //1: alive, 2: neighbours.

    public GameOfLife(Environment env) throws RemoteException {
        super();
        this.env = env;
        this.pos = new AtomicInteger(0);

        this.step = 1;
        this.alive = new HashSet<Integer>(ENV_LENGTH);
        this.lastIndex = new AtomicInteger(0);
        this.neighbours = new HashMap<Integer, Integer>();
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
        this.env = null;
    }

    public void checkCompletion() {
        if (this.pos.get() > ENV_LENGTH) {
            this.status = Status.WAIT;
            this.env.nextGeneration();
            //System.gc();

            System.out.println("--- Generation completed in " + (System.currentTimeMillis() - before_generation) + " ms ---");
            this.before_generation = 0;


            if (CONFIG.RENDER_ACTIVE) {
                EventDispatcher.instance.publish(new UpdateSpritesEvent(), ThreadID.Render);
            } else {
                this.status = Status.CONTINUE;
            }

            this.pos.set(0);
        }
    }

    public void _checkCompletion() {
        if (this.lastIndex.get() > ENV_LENGTH) {
            this.status = Status.WAIT;
            if (step == 1) {
                this.alive.clear();
                this.alive = new HashSet<Integer>(this.next_alive);
                this.next_alive.clear();

                this.neighbours.clear();
                this.neighbours = new HashMap<Integer, Integer>(this.next_neighbours);
                this.next_neighbours.clear();

                step = 2;
                this.lastIndex.set(0);
                this.status = Status.CONTINUE;
            }
            else if(step == 2) {
                this.alive.clear();
                this.alive = new HashSet<Integer>(this.next_alive);
                this.next_alive.clear();

                this.neighbours.clear();
                this.neighbours = new HashMap<Integer, Integer>(this.next_neighbours);
                this.next_neighbours.clear();

                System.out.println("--- Generation completed in " + (System.currentTimeMillis() - before_generation) + " ms ---");
                this.before_generation = 0;

                step = 1;
                this.lastIndex.set(0);
                this.status = Status.CONTINUE;
            }
        }
    }

    @Override
    public IGOLProcess getNext() throws RemoteException {
        if (this.before_generation == 0) {
            this.before_generation = System.currentTimeMillis();
        }
        this.checkCompletion();
        int currPos = this.pos.getAndIncrement();
        return new GOLProcess(currPos, this.env.getSubEnv(currPos));
    }

    public IGOLProcess _getNext() throws RemoteException {
        this.checkCompletion();
        if (this.step == 1) {
            HashSet<Integer> subset = this.alive.stream()
                    .skip(lastIndex.getAndAdd(CHUNK_SIZE))
                    .limit(CHUNK_SIZE)
                    .collect(Collectors.toCollection(HashSet::new));

            return new PurgeProcess(subset, this.neighbours);
        } else if (this.step == 2) {
            Map<Integer, Integer> subset = this.neighbours.entrySet().stream()
                    .skip(lastIndex.getAndAdd(CHUNK_SIZE))
                    .limit(CHUNK_SIZE)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new GenerateProcess(this.alive, subset);
        }

        return null;
    }

    @Override
    public void sendResult(IGOLProcess t) throws RemoteException {
        if (t != null) {
            GOLProcess task = (GOLProcess) t;
            env.setCellState(task.pos, task.updatedState());
        }
    }

    public void _sendResult(IGOLProcess t) throws RemoteException {
        if (t != null) {
            this.next_alive.addAll(t.getNextAlive());
            this.next_neighbours.entrySet().addAll(t.getNextNeighbours().entrySet());
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
