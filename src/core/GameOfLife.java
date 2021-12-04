package core;

import network.Server;
import network.Status;

import java.util.concurrent.atomic.AtomicInteger;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife implements IGameOfLife {
    public Environment env;
    public AtomicInteger currentTask;

    public GameOfLife(Environment env) {
        this.env = env;
    }

    public IGOLProcess getNext() {
        if (currentTask.get() < ENV_SIZE * ENV_SIZE) {
            int taskIndex = currentTask.incrementAndGet();

            int x = taskIndex % ENV_SIZE;
            int y = taskIndex / ENV_SIZE;

            Cell[][] local_env = env.getSubEnv(x, y);
            return new GOLProcess(x, y, local_env);
        } else {
            Server.instance.setStatus(Status.WAIT);
            return null;
        }
    }

    public void sendResult(IGOLProcess t) {
        GOLProcess task = (GOLProcess) t;
        env.setCellState(task.x, task.y, task.updatedState());
    }
}
