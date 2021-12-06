package core;

import java.io.Serializable;

public class GOLProcess implements IGOLProcess, Serializable {
    private final Cell[][] local_env;
    public final int x;
    public final int y;
    private Cell result;

    public GOLProcess(int x, int y, Cell[][] local_env) {
        this.x = x;
        this.y = y;
        this.local_env = local_env;
    }

    public Cell updatedState() {
        return this.result;
    }

    public void run() {
        int aliveNeighbours = getAliveNeighbours();
        System.out.println(aliveNeighbours);
        if (aliveNeighbours == 3) { //naissance
            this.result = Cell.Alive;
            System.out.println("alive");
        }
        else if (aliveNeighbours == 2) { //état courant
            this.result = local_env[1][1];
            System.out.println("current: " + this.result);
        }
        else { //mort
            this.result = Cell.Empty;
            System.out.println("dead");
        }
    }


    //TODO: optimisation ?
    private int getAliveNeighbours() {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.local_env[i][j] == Cell.Alive){
                    counter++;
                }
            }
        }
        return counter;
    }
}
