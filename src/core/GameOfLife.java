package core;

import graphics.SpriteManager;
import network.Status;

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

            /*
            try {
                Thread.currentThread().sleep(RENDER_TICK);
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
             */

            this.x = 0;
            this.y = 0;
            this.status = Status.CONTINUE;
        }
    }

    public synchronized IGOLProcess getNext() {
        if (y < ENV_SIZE && x < ENV_SIZE) {
            Cell[][] local_env = env.getSubEnv(x, y);
            GOLProcess task = new GOLProcess(x, y, local_env);
            y++;
            return task;
        } else {
            y = 0;

            if (x + 1 < ENV_SIZE) x++;
            else this.status = Status.WAIT;

            return null;
        }
    }

    public synchronized void sendResult(IGOLProcess t) {
        if (t != null) {
            GOLProcess task = (GOLProcess) t;
            env.setCellState(task.x, task.y, task.updatedState());
        }
    }

    public Status getStatus() {
        return this.status;
    }
}
