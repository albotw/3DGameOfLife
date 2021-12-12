package core.v2;

import events.EventDispatcher;
import events.Events.CycleDone;
import events.ThreadID;
import network.Server;
import network.Status;

import java.util.HashSet;

import static CONFIG.CONFIG.ENV_SIZE;

public class GameOfLife {
    private HashSet<Cell> currentAliveCells;
    private HashSet<Cell> nextAliveCells;
    private Status status;
    private int x;
    private int y;
    private int z;

    public GameOfLife() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.currentAliveCells = new HashSet<Cell>(ENV_SIZE * ENV_SIZE * ENV_SIZE);
        this.nextAliveCells = new HashSet<Cell>(ENV_SIZE * ENV_SIZE * ENV_SIZE);
    }

    public void checkCompletion() {
        if (this.status == status.WAIT)
        {
            this.nextGeneration();

            this.x = 0;
            this.y = 0;
            this.z = 0;

            this.status = Status.CONTINUE;
            System.out.println();
            System.out.println("### NEXT GENERATION ###");
            System.out.println("Alive cells: " + this.currentAliveCells.size());
        }
    }

    public void randomValues(int quantity) {
        int counter = 0;
        do {
            int x = (int) (Math.random() * ENV_SIZE);
            int y = (int) (Math.random() * ENV_SIZE);
            int z = (int) (Math.random() * ENV_SIZE);

            if (this.currentAliveCells.add(new Cell(x, y ,z))) {
                System.out.println("added cell at " + x + " " + y + " " + z);
                counter++;
            }
        } while (counter < quantity);
    }

    public Status getStatus() {
        return this.status;
    }
    public void setStatus(Status s) {this.status = s;}

    public Cell getNext() {
        if (x < ENV_SIZE && y < ENV_SIZE && z < ENV_SIZE) {
             Cell out =  new Cell(this.x, this.y, this.z);
             x++;
             return out;
        }else {
            x = 0;
            if (y + 1 < ENV_SIZE) y++;
            else {
                y = 0;
                if (z + 1 < ENV_SIZE) z++;
                else {
                    this.status = status.WAIT;
                    Server.instance.finishCycle();
                }
            }
        }

        return null;
    }

    public void setCell(Cell c) {
        nextAliveCells.add(c);
    }

    public boolean isAlive(Cell c) {
        return this.currentAliveCells.contains(c);
    }

    public void nextGeneration() {
        this.currentAliveCells.clear();
        this.currentAliveCells.addAll(this.nextAliveCells);
        this.nextAliveCells.clear();
    }
}
