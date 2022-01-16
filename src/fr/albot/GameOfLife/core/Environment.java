package fr.albot.GameOfLife.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;

public class Environment {
    private HashSet<Integer> alive;
    private ConcurrentHashMap<Integer, Integer> neighbours; // position, nombre de voisins.
    private Iterator<Map.Entry<Integer, Integer>> neighboursIterator;

    private static int envSize;
    private static int envLength;

    public Environment() {
        init();
    }

    public static int to1d(int x, int y, int z) {
        return (x + (y * envSize) + z * (envSize * envSize));
    }

    public static int[] to3d(int index) {
        int z = index / (envSize * envSize);
        index -= z * envSize * envSize;
        int x = index % envSize;
        int y = index / envSize;

        return new int[]{x, y, z};
    }

    public static boolean inBounds(int x, int y, int z) {
        return (x >= 0 && x < envSize && y >= 0 && y < envSize && z >= 0 && z < envSize);
    }

    public void init() {
        Environment.envSize = ENV_SIZE;
        Environment.envLength = ENV_LENGTH;
        this.alive = new HashSet<Integer>(envLength);
        this.neighbours = new ConcurrentHashMap<Integer, Integer>();
    }

    public void purge() {
        this.alive.clear();
        this.neighbours.clear();
        this.alive = null;
        this.neighbours = null;
    }

    public void randomValues() {
        int counter = 0;
        do {
            int i = (int) (Math.random() * envSize);
            int j = (int) (Math.random() * envSize);
            int k = (int) (Math.random() * envSize);
            int cell = Environment.to1d(i, j, k);

            if (!this.alive.contains(cell)) {
                this.alive.add(cell);

                generateNeighbours(i, j, k);

                counter++;
            }
        } while (counter < RAND_CELLS);

        System.out.println("generated " + this.alive.size() + " alive cells");
        System.out.println("generated " + this.neighbours.size() + " neighbours");
    }

    public void loadFromFile(String path) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();

            while (line != null) {
                String[] pos = line.split(" ");
                int i = Integer.parseInt(pos[0]);
                int j = Integer.parseInt(pos[1]);
                int k = Integer.parseInt(pos[2]);

                int cell = Environment.to1d(i, j, k);
                if (!this.alive.contains(cell)) {
                    this.alive.add(cell);
                    generateNeighbours(i, j, k);
                }
                line = br.readLine();
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateNeighbours(int i, int j, int k) {
        for (int x = i - 1; x <= i + 1; x++) {
            for (int y = j - 1; y <= j + 1; y++) {
                for (int z = k - 1; z <= k + 1; z++) {

                    if (!(x == i && y == j && z == k) && inBounds(x, y, z)) {
                        int neighbour = Environment.to1d(x, y, z);
                        if (this.neighbours.containsKey(neighbour)) {
                            this.neighbours.put(neighbour, this.neighbours.get(neighbour) + 1);
                        } else {
                            this.neighbours.put(neighbour, 1);
                        }
                    }
                }
            }
        }

        this.neighboursIterator = this.neighbours.entrySet().iterator();
    }

    public void nextGeneration(ArrayList<Integer> toDelete, ArrayList<Integer> toCreate) {
        System.out.println("cells to create: " + toCreate.size());
        for (Integer cell : toCreate) {
            this.alive.add(cell);

            int[] pos = Environment.to3d(cell);
            int i = pos[0];
            int j = pos[1];
            int k = pos[2];
            for (int x = i - 1; x <= i + 1; x++) {
                for (int y = j - 1; y <= j + 1; y++) {
                    for (int z = k - 1; z <= k + 1; z++) {

                        if (!(x == i && y == j && z == k) && inBounds(x, y, z)) {
                            int neighbour = Environment.to1d(x, y, z);
                            Integer value = this.neighbours.get(neighbour);
                            this.neighbours.put(neighbour, value != null ? value + 1 : 1);
                        }
                    }
                }
            }
        }


        System.out.println("cells to delete: " + toDelete.size());
        for (Integer cell : toDelete) {
            this.alive.remove(cell);

            int[] pos = Environment.to3d(cell);
            int i = pos[0];
            int j = pos[1];
            int k = pos[2];
            for (int x = i - 1; x <= i + 1; x++) {
                for (int y = j - 1; y <= j + 1; y++) {
                    for (int z = k - 1; z <= k + 1; z++) {

                        if (!(x == i && y == j && z == k) && inBounds(x, y, z)) {
                            int neighbour = Environment.to1d(x, y, z);
                            Integer value = this.neighbours.get(neighbour);
                            if (value != null) {
                                value--;
                                if (value > 0) {
                                    this.neighbours.put(neighbour, value);
                                } else {
                                    this.neighbours.remove(neighbour);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.neighboursIterator = this.neighbours.entrySet().iterator();

        System.out.println("alive: " + this.alive.size());
        System.out.println("neighbours: " + this.neighbours.size());
    }

    public int getAliveSize() {
        return this.alive.size();
    }

    public int getNeighboursSize() {
        return this.neighbours.size();
    }

    public HashSet<Integer> getAlive() {
        return this.alive;
    }

    public ConcurrentHashMap<Integer, Integer> getNeighbours() {
        return this.neighbours;
    }

    public HashSet<Integer> getAliveSubset(int lastIndex) {
        return this.alive.stream()
                .skip(lastIndex)
                .limit(CHUNK_SIZE)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public ConcurrentHashMap<Integer, Integer> getNeighboursSubset(int lastIndex) {
        ConcurrentHashMap<Integer, Integer> subset = new ConcurrentHashMap<Integer, Integer>();
        int begin = lastIndex;
        int end = begin + CHUNK_SIZE;
        for (int i = begin; i < end; i++) {
            if (this.neighboursIterator.hasNext()) {
                Map.Entry<Integer, Integer> pair = this.neighboursIterator.next();
                subset.put(pair.getKey(), pair.getValue());
            }
        }
        return subset;
    }
}
