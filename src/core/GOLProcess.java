package core;

import java.io.Serializable;

import static CONFIG.CONFIG.*;

public class GOLProcess implements IGOLProcess, Serializable {
    private final Cell[][][] local_env;
    public final int x;
    public final int y;
    public final int z;
    private Cell result;

    public GOLProcess(int x, int y, int z, Cell[][][] local_env) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.local_env = local_env;
    }

    public Cell updatedState() {
        return this.result;
    }

    public void run() {
        int aliveNeighbours = getAliveNeighbours();
        System.out.println(this.x + " " + this.y + " " + this.z + " " + aliveNeighbours);
        if (aliveNeighbours == ALIVE_THRESOLD) { //naissance
            this.result = Cell.Alive;
        } else if (aliveNeighbours == CURRENT_THRESHOLD) { //état courant
            this.result = local_env[1][1][1];
        } else { //mort
            this.result = Cell.Empty;
        }
    }


    //TODO: optimisation ?
    private int getAliveNeighbours() {
        int counter = 0;
        int middle = Math.floorDiv(SUB_ENV_SIZE, 2);
        for (int i = 0; i < SUB_ENV_SIZE; i++) {
            for (int j = 0; j < SUB_ENV_SIZE; j++) {
                for (int k = 0; k < SUB_ENV_SIZE; k++) {
                    if (!(i == middle && j == middle && k == middle)) {
                        if (this.local_env[i][j][k] == Cell.Alive) {
                            counter++;
                        }
                    }
                }
            }
        }
        return counter;
    }
}
