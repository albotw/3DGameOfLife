import core.Cell;
import core.Environment;

import static CONFIG.CONFIG.*;
import static CONFIG.CONFIG.CURRENT_THRESHOLD;

public class App {
    public static void main(String[] args) {
        Environment env = new Environment();
        env.randomValues(500000);

        while(true) {
            long before_cycle = System.currentTimeMillis();

            for (int x = 0; x < ENV_SIZE; x++) {
                for (int y = 0; y < ENV_SIZE; y++) {
                    for (int z = 0; z < ENV_SIZE; z++) {
                        long before_cell = System.currentTimeMillis();
                        int counter = 0;
                        int middle = Math.floorDiv(SUB_ENV_SIZE, 2);
                        for (int i = 0; i < SUB_ENV_SIZE; i++) {
                            for (int j = 0; j < SUB_ENV_SIZE; j++) {
                                for (int k = 0; k < SUB_ENV_SIZE; k++) {
                                    if (!(i == middle && j == middle && k == middle)) {
                                        if (env.getCellState(i, j, k) == Cell.Alive)
                                            counter++;
                                    }
                                }
                            }
                        }

                        if (counter == ALIVE_THRESOLD) { //naissance
                            env.setCellState(x, y, z, Cell.Alive);
                        } else if (counter == CURRENT_THRESHOLD) { //état courant
                            env.setCellState(x, y, z, env.getCellState(x,y, z));
                        } else { //mort
                            env.setCellState(x, y, z, Cell.Empty);
                        }
                    }
                }
            }

            System.out.println("Generation processed in " + (System.currentTimeMillis() - before_cycle) + " ms using " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes");
            System.out.println();
            try {
                Thread.currentThread().sleep(1000);
            }catch (Exception e) {e.printStackTrace();}

            env.nextGeneration();
            System.gc();
        }
    }
}
