package core;
import static CONFIG.CONFIG.SUB_ENV_SIZE;
import static CONFIG.CONFIG.ENV_SIZE;

public class Environment {

    private Cell[][] current_env;
    private Cell[][] future_env;

    public Environment() {
        this.current_env = new Cell[ENV_SIZE][ENV_SIZE];
        this.future_env = new Cell[ENV_SIZE][ENV_SIZE];

        this.clean(current_env);
        this.clean(future_env);
    }

    public void initValues(int[] positions)
    {
        for (int i = 0; i < positions.length; i += 2) {
            current_env[positions[i]][positions[i+1]] = new Cell(CellState.Alive);
            System.out.println("added cell at " + positions[i] + " " + positions[i+1]);
        }
    }

    private void clean(Cell[][] env) {
        for (int i = 0; i < ENV_SIZE; i++)
        {
            for (int j = 0; j < ENV_SIZE; j++)
            {
                env[i][j] = new Cell(CellState.Empty);
            }
        }
    }

    public Cell[][] getSubEnv(int x, int y) {
        Cell[][] sub_env = new Cell[SUB_ENV_SIZE][SUB_ENV_SIZE];

        int offset = SUB_ENV_SIZE % 2;
        for (int i = 0; i < SUB_ENV_SIZE; i++) {
            for (int j = 0; j < SUB_ENV_SIZE; j++) {
                sub_env[i][j] = getCellState(x - offset, y - offset);
            }
        }

        return sub_env;
    }

    public void print() {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                if (this.current_env[i][j].state == CellState.Alive) System.out.print("X ");
                if (this.current_env[i][j].state == CellState.Empty) System.out.print("O ");
            }
            System.out.println();
        }
    }

    public Cell getCellState(int x, int y) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE)
        {
            if (this.current_env[x][y] == null) {
                return new Cell(CellState.Empty);
            }
            else {
                return this.current_env[x][y];
            }
        }
        else return new Cell(CellState.Empty);
    }

    public void setCellState(int x, int y, Cell cell) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE)
        {
            this.future_env[x][y].state = cell.state;
            System.out.println(cell.state);
        }
    }

    public void nextGeneration()
    {
        for (int i = 0; i < ENV_SIZE; i++) {
            for (int j = 0; j < ENV_SIZE; j++) {
                this.current_env[i][j] = this.future_env[i][j];
            }
        }
        clean(this.future_env);
    }
}
