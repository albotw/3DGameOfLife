package net;

import core.Cell;

import java.io.Serializable;

public class Task implements ITask, Serializable {
    private Cell[] local_env;
    private int x;
    private int y;
    private Cell result;

    public Task(int x, int y, Cell[] local_env) {
        this.x = x;
        this.y = y;
        this.local_env = local_env;
    }

    public Cell updatedState() {
        return Cell.Alive;
    }

    public void run() {
        /**
         * TODO
         * GOL pour la case [x][y] avec env prédéfini
         */
        this.result = Cell.Alive;
    }
}
