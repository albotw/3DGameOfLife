package core.v2;

import java.util.HashSet;
import java.util.Iterator;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife {
    private HashSet<Cell> current_env;
    private Iterator<Cell> current_iterator;
    private HashSet<Cell> future_env;
    private HashSet<Cell> toProcess;

    public GameOfLife() {
        this.current_env = new HashSet<Cell>(ENV_SIZE * ENV_SIZE * ENV_SIZE);
        this.current_iterator = current_env.iterator();
        this.future_env = new HashSet<Cell>(ENV_SIZE * ENV_SIZE * ENV_SIZE);
    }

    public synchronized Cell getCurrentCell() {
        if (this.current_iterator.hasNext()) {
            return this.current_iterator.next();
        } else return null;
    }

    public synchronized void addToProcessCell(Cell c) {
        this.toProcess.add(c);
    }

    public synchronized boolean toProcess_contains(Cell c) {
        return this.toProcess_contains(c);
    }

    public synchronized Cell getCell() {

    }

    public boolean isAlive(Cell c) {
        return this.current_env.contains(c);
    }

}
