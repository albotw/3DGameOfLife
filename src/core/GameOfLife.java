package core;

import java.util.concurrent.atomic.AtomicInteger;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife implements IGameOfLife {
    public Environment env;
    public AtomicInteger currentTask;

    public GameOfLife(int N) {

    }

    public IGOLProcess getNext() {
        int taskIndex = currentTask.incrementAndGet();

        int x = taskIndex % ENV_SIZE;
        int y = taskIndex / ENV_SIZE;

        Cell[][] local_env = env.getSubEnv(x, y);
        return new GOLProcess(x, y, local_env);
    }

    public void sendResult(IGOLProcess t) {
        GOLProcess task = (GOLProcess)t;
        env.setCellState(task.x, task.y, task.updatedState());
    }

    public void getCurrentEnv() {
        //TODO récupération du tableau en cours pour affichage.
    }
}
