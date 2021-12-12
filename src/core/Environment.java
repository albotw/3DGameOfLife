package core;

import static CONFIG.CONFIG.ENV_SIZE;
import static CONFIG.CONFIG.SUB_ENV_SIZE;

public class Environment {

    private Cell[][][] current_env;
    private Cell[][][] future_env;

    public Environment() {
        this.current_env = new Cell[ENV_SIZE][ENV_SIZE][ENV_SIZE];
        this.future_env = new Cell[ENV_SIZE][ENV_SIZE][ENV_SIZE];

        this.clean(current_env);
        this.clean(future_env);
    }

    public void initValues(int[] positions) {
        for (int i = 0; i < positions.length; i += 3) {
            current_env[positions[i]][positions[i + 1]][positions[i + 2]] = Cell.Alive;
            System.out.println("added cell at " + positions[i] + " " + positions[i + 1] + " " + positions[i + 2]);
        }
    }

    public void randomValues(int quantity) {
        int counter = 0;
        do {
            int x = (int) (Math.random() * ENV_SIZE);
            int y = (int) (Math.random() * ENV_SIZE);
            int z = (int) (Math.random() * ENV_SIZE);

            if (this.current_env[x][y][z] == Cell.Empty) {
                this.current_env[x][y][z] = Cell.Alive;
                System.out.println("added cell at " + x + " " + y + " " + z);
                counter++;
            }
        } while (counter < quantity);
    }

    public int size() {
        return this.current_env.length;
    }

    private void clean(Cell[][][] env) {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                for (int k = 0; k < ENV_SIZE; k++) {
                    env[i][j][k] = Cell.Empty;
                }
            }
        }
    }

    public void purge() {
        this.current_env = null;
        this.future_env = null;
    }

    public Cell[][][] getSubEnv(int x, int y, int z) {
        Cell[][][] sub_env = new Cell[SUB_ENV_SIZE][SUB_ENV_SIZE][SUB_ENV_SIZE];

        int offset = SUB_ENV_SIZE % 2;
        for (int i = 0; i < SUB_ENV_SIZE; i++) {
            for (int j = 0; j < SUB_ENV_SIZE; j++) {
                for (int k = 0; k < SUB_ENV_SIZE; k++) {
                    sub_env[i][j][k] = getCellState((x - offset) + i, (y - offset) + j, (z - offset) + j);
                }
            }
        }

        return sub_env;
    }

    public synchronized Cell getCellState(int x, int y, int z) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE && z >= 0 && z < ENV_SIZE) {
            return this.current_env[x][y][z];
        } else return Cell.Empty;
    }

    public synchronized void setCellState(int x, int y, int z, Cell cell) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE && z >= 0 && z < ENV_SIZE) {
            this.future_env[x][y][z] = cell;
        }
    }

    public void nextGeneration() {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                for (int k = 0; k < ENV_SIZE; k++) {
                    this.current_env[i][j][k] = this.future_env[i][j][k];
                }
            }
        }
        clean(this.future_env);
    }
}
