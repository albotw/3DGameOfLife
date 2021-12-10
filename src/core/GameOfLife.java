package core;

import events.Event;
import events.EventDispatcher;
import events.EventQueue;
import events.Events.KillEvent;
import events.Events.SpriteUpdateDoneEvent;
import events.Events.UpdateSpritesEvent;
import events.ThreadID;
import network.Status;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife extends Thread implements IGameOfLife {
    public Environment env;
    private EventQueue eventQueue;
    private int x;
    private int y;
    private int z;
    public Status status;

    public GameOfLife(Environment env) {
        this.eventQueue = new EventQueue(ThreadID.App);
        this.env = env;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.status = Status.CONTINUE;
    }

    public void run() {
        boolean running = true;
        while (running) {
            if (!this.eventQueue.isEmpty()) {
                Event e = this.eventQueue.get();
                if (e instanceof SpriteUpdateDoneEvent) {
                    this.status = Status.CONTINUE;
                }
                if (e instanceof KillEvent) {
                    running = false;
                }
            }
        }
        this.eventQueue.purge();
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

            /*
            try {
                Thread.currentThread().sleep(RENDER_TICK);
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
             */

            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
    }

    public synchronized IGOLProcess getNext() {
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
                }
            }

            return null;
        }
    }

    public synchronized void sendResult(IGOLProcess t) {
        if (t != null) {
            GOLProcess task = (GOLProcess) t;
            env.setCellState(task.x, task.y, task.z, task.updatedState());
        }
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
