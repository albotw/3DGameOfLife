package fr.albot.GameOfLife;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.core.Cell;
import fr.albot.GameOfLife.core.Environment;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;

public class App {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-nodisplay":
                        CONFIG.RENDER_ACTIVE = false;
                        System.out.println("Disabled rendering");
                        break;
                    case "-envsize":
                        CONFIG.ENV_SIZE = Integer.parseInt(args[i + 1]);
                        System.out.println("Set env_size to " + CONFIG.ENV_SIZE);
                        break;
                    case "-cells":
                        RAND_CELLS = Integer.parseInt(args[i + 1]);
                        System.out.println("Will generate " + RAND_CELLS + " cells at random positions");
                        break;
                    case "-alive":
                        CONFIG.ALIVE_THRESHOLD = Integer.parseInt(args[i + 1]);
                        System.out.println(CONFIG.ALIVE_THRESHOLD + " alive neighbours for a cell to be alive");
                        break;
                    case "-current":
                        CONFIG.CURRENT_THRESHOLD = Integer.parseInt(args[i + 1]);
                        System.out.println(CONFIG.CURRENT_THRESHOLD + " alive neighbours to keep current state");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        Environment env = new Environment();
        env.randomValues(RAND_CELLS);

        while (true) {
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

                        if (counter == ALIVE_THRESHOLD) { //naissance
                            env.setCellState(x, y, z, Cell.Alive);
                        } else if (counter == CURRENT_THRESHOLD) { //état courant
                            env.setCellState(x, y, z, env.getCellState(x, y, z));
                        } else { //mort
                            env.setCellState(x, y, z, Cell.Empty);
                        }
                    }
                }
            }

            System.out.println("Generation processed in " + (System.currentTimeMillis() - before_cycle) + " ms using " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes");
            System.out.println();
            try {
                //Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            env.nextGeneration();
            System.gc();
        }
    }
}
