package net;

import core.Environment;

public class BagOfTask implements IBagOfTask{
    public Environment env;
    public volatile int currentTask;

    public BagOfTask(int N) {

    }

    public ITask getNext() {
        currentTask++;
        //TODO: déterminer environnement de la cellule à partir de l'index actuel
        return null;
    }

    public void sendResult(ITask t) {
        Task task = (Task)t;
        //TODO: récupération de la cellule traitée et mise a jour de l'état.
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean[] getTab() {
        return new boolean[0];
    }
}
