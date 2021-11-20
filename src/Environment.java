public class Environment {
    public static final int ENV_SIZE = 3;

    private Cell[][] env;
    private Cell[][] future_env;

    public Environment() {
        env = new Cell[ENV_SIZE][ENV_SIZE];
        future_env = new Cell[ENV_SIZE][ENV_SIZE];

        clean(env);
        clean(future_env);
    }

    public void initValues(int[] positions)
    {
        for (int i = 0; i < positions.length; i += 2) {
            env[positions[i]][positions[i+1]] = Cell.Alive;
        }
    }

    private void clean(Cell[][] env) {
        for (int i = 0; i < ENV_SIZE; i++)
        {
            for (int j = 0; j < ENV_SIZE; j++)
            {
                env[i][j] = Cell.Empty;
            }
        }
    }

    public Cell getCellState(int x, int y) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE)
        {
            return this.env[x][y];
        }
        else return Cell.Empty;
    }

    public void setCellState(int x, int y, Cell state) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE)
        {
            this.future_env[x][y] = state;
        }
    }

    public void nextGeneration()
    {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                this.env[i][j] = this.future_env[i][j];
            }
        }
        clean(this.future_env);
    }

    public void print() {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                if (this.env[i][j] == Cell.Alive) System.out.print("X ");
                if (this.env[i][j] == Cell.Empty) System.out.print("O ");
            }

            System.out.println();
        }
        System.out.println();
    }
}
