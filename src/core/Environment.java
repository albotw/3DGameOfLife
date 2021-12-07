package core;

import static CONFIG.CONFIG.ENV_SIZE;
import static CONFIG.CONFIG.SUB_ENV_SIZE;

public class Environment {

    private Cell[][] current_env;
    private Cell[][] future_env;

    public Environment() {
        this.current_env = new Cell[ENV_SIZE][ENV_SIZE];
        this.future_env = new Cell[ENV_SIZE][ENV_SIZE];

        this.clean(current_env);
        this.clean(future_env);
    }

    public void initValues(int[] positions) {
        for (int i = 0; i < positions.length; i += 2) {
            current_env[positions[i]][positions[i + 1]] = Cell.Alive;
            System.out.println("added cell at " + positions[i] + " " + positions[i + 1]);
        }
    }

    public void randomValues(int quantity) {
        int counter = 0;
        do {
            int x = (int) (Math.random() * ENV_SIZE);
            int y = (int) (Math.random() * ENV_SIZE);

            if (this.current_env[x][y] == Cell.Empty) {
                this.current_env[x][y] = Cell.Alive;
                System.out.println("added cell at " + x + " " + y);
                counter++;
            }
        } while (counter < quantity);
    }

    private void clean(Cell[][] env) {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                env[i][j] = Cell.Empty;
            }
        }
    }

    public Cell[][] getSubEnv(int x, int y) {
        Cell[][] sub_env = new Cell[SUB_ENV_SIZE][SUB_ENV_SIZE];

        int offset = SUB_ENV_SIZE % 2;
        for (int i = 0; i < SUB_ENV_SIZE; i++) {
            for (int j = 0; j < SUB_ENV_SIZE; j++) {
                sub_env[i][j] = getCellState((x - offset) + i, (y - offset) + j);
            }
        }

        return sub_env;
    }

    public Cell getCellState(int x, int y) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE) {
            return this.current_env[x][y];
        } else return Cell.Empty;
    }

    public void setCellState(int x, int y, Cell cell) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE) {
            this.future_env[x][y] = cell;
        }
    }

    public void nextGeneration() {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                this.current_env[i][j] = this.future_env[i][j];
            }
        }
        clean(this.future_env);
    }
}
