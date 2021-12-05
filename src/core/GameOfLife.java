package core;

import network.Status;

import java.util.concurrent.atomic.AtomicInteger;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife implements IGameOfLife {
    public Environment env;
    public AtomicInteger currentTask;
    public Status status;

    public GameOfLife(Environment env) {
        this.env = env;
        this.currentTask = new AtomicInteger(0);
        this.status = Status.CONTINUE;
    }

    public void checkCompletion() {
        if (this.status == Status.WAIT) {
            this.env.nextGeneration();
            this.currentTask.set(0);
            System.out.println("next generation");
            this.status = Status.CONTINUE;
        }
    }

    public IGOLProcess getNext() {
        int taskIndex = currentTask.incrementAndGet();
        if (taskIndex <= ENV_SIZE * ENV_SIZE) {
            int x = taskIndex % ENV_SIZE;
            int y = taskIndex / ENV_SIZE;

            Cell[][] local_env = env.getSubEnv(x, y);
            System.out.println("TaskID: " + currentTask.get());
            return new GOLProcess(x, y, local_env);
        } else {
            this.status = Status.WAIT;
            return null;
        }
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
