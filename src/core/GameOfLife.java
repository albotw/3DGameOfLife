package core;

import graphics.SpriteManager;
import network.Status;

import java.util.concurrent.atomic.AtomicInteger;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife implements IGameOfLife {
    public Environment env;
    private int x;
    private int y;
    public Status status;

    public GameOfLife(Environment env) {
        this.env = env;
        this.x = 0;
        this.y = 0;
        this.status = Status.CONTINUE;
    }

    public void checkCompletion() {
        if (this.status == Status.WAIT) {
            this.env.nextGeneration();
            SpriteManager.instance.displayEnv();
            this.x = 0;
            this.y = 0;
            this.status = Status.CONTINUE;
        }
    }

    public IGOLProcess getNext() {
        Cell[][] local_env = env.getSubEnv(x, y);
        GOLProcess task =  new GOLProcess(x, y, local_env);

        if (++y >= ENV_SIZE) {
            y = 0;
            if (++x >= ENV_SIZE) {
                this.status = Status.WAIT;
            }
        }

        System.out.println(x + " " + y);
        return task;
    }

    public void sendResult(IGOLProcess t) {
        if (t != null) {
            GOLProcess task = (GOLProcess) t;
            env.setCellState(task.x, task.y, task.updatedState());
        }
    }

    public Status getStatus() {
        return this.status;
    }
}
