package fr.albot.GameOfLife;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.core.Environment;
import fr.albot.GameOfLife.core.Pattern;
import fr.albot.GameOfLife.core.distributed.DistributedGameOfLife;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;

public class SingleHash {

    private static DistributedGameOfLife gol;

    public static void main(String[] args) throws RemoteException {
        try {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-nodisplay":
                        CONFIG.RENDER_ACTIVE = false;
                        System.out.println("Disabled rendering");
                        break;
                    case "-file":
                        CONFIG.PATTERN = Pattern.CUSTOM;
                        CONFIG.PATTERN_FILE = args[i + 1];
                        System.out.println("Will load env from " + CONFIG.PATTERN_FILE);
                        break;
                    case "-envsize":
                        CONFIG.ENV_SIZE = Integer.parseInt(args[i + 1]);
                        RAND_CELLS = (CONFIG.ENV_SIZE * CONFIG.ENV_SIZE * CONFIG.ENV_SIZE) / 2;
                        System.out.println("Set env_size to " + CONFIG.ENV_SIZE);
                        break;
                    case "-chunksize":
                        CONFIG.CHUNK_SIZE = Integer.parseInt(args[i + 1]);
                        System.out.println("Set chunk_size to " + CONFIG.CHUNK_SIZE);
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

        loop();
    }

    public static void loop() {
        Environment env = new Environment();
        env.randomValues();

        while (true) {
            long before = System.currentTimeMillis();

            ArrayList<Integer> toCreate = new ArrayList();
            ArrayList<Integer> toDelete = new ArrayList();
            for (int cell : env.getAlive()) {
                Integer n = env.getNeighbours().get(cell);
                if (n == null || n < CURRENT_THRESHOLD || n > ALIVE_THRESHOLD) {
                    toDelete.add(cell);
                }
            }

            for (int cell : env.getNeighbours().keySet()) {
                Integer aliveNeighbours = env.getNeighbours().get(cell);
                if ((aliveNeighbours == ALIVE_THRESHOLD || aliveNeighbours == CURRENT_THRESHOLD) && !env.getAlive().contains(cell)) {
                    toCreate.add(cell);
                }
            }

            env.nextGeneration(toDelete, toCreate);
            System.out.println("--- Generation completed in " + (System.currentTimeMillis() - before) + " ms ---");

        }
    }
}
