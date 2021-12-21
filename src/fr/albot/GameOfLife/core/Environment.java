package fr.albot.GameOfLife.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;

public class Environment {
    private HashSet<Integer> alive;
    private HashMap<Integer, Integer> neighbours; // position, nombre de voisins.

    public Environment() {
        this.alive = new HashSet<Integer>(ENV_LENGTH);
        this.neighbours = new HashMap<Integer, Integer>();
    }

    public static int to1d(int x, int y, int z) {
        return (x + (y * ENV_SIZE) + z * (ENV_SIZE * ENV_SIZE));
    }

    public static int[] to3d(int index) {
        int z = index / (ENV_SIZE * ENV_SIZE);
        index -= z * ENV_SIZE * ENV_SIZE;
        int x = index % ENV_SIZE;
        int y = index / ENV_SIZE;

        return new int[]{x, y, z};
    }

    public static boolean inBounds(int x, int y, int z) {
        return (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE && z >= 0 && z < ENV_SIZE);
    }

    public void blinker() {
        int[] positions = {
                5, 5, 5,
                4, 5, 5,
                6, 5, 5,
                5, 4, 5,
                5, 6, 5,
                5, 5, 4,
                5, 5, 6
        };

        for (int cell = 0; cell < positions.length; cell += 3) {
            int i = positions[cell];
            int j = positions[cell + 1];
            int k = positions[cell + 2];
            int hash = Environment.to1d(i, j, k);
            this.alive.add(hash);

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
        }

        System.out.println("Generated blinker pattern");
        System.out.println("generated " + this.alive.size() + " alive cells");
        System.out.println("generated " + this.neighbours.size() + " neighbours");
    }

    public void randomValues() {
        int counter = 0;
        do {
            int i = (int) (Math.random() * ENV_SIZE);
            int j = (int) (Math.random() * ENV_SIZE);
            int k = (int) (Math.random() * ENV_SIZE);
            int cell = Environment.to1d(i, j, k);

            if (!this.alive.contains(cell)) {
                this.alive.add(cell);

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

                counter++;
            }
        } while (counter < RAND_CELLS);

        System.out.println("generated " + this.alive.size() + " alive cells");
        System.out.println("generated " + this.neighbours.size() + " neighbours");
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

    public HashMap<Integer, Integer> getNeighbours() {
        return this.neighbours;
    }

    public HashSet<Integer> getAliveSubset(int lastIndex) {
        return this.alive.stream()
                .skip(lastIndex)
                .limit(CHUNK_SIZE)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public HashMap<Integer, Integer> getNeighboursSubset(int lastIndex) {
        HashMap<Integer, Integer> subset = new HashMap<Integer, Integer>();
        int begin = lastIndex;
        for (int i = begin; i < begin + CHUNK_SIZE; i++) {
            if (i < this.neighbours.size()) {
                Integer key = (Integer) this.neighbours.keySet().toArray()[i];
                subset.put(key, this.neighbours.get(key));
            } else {
                i = begin + CHUNK_SIZE + 1;
            }
        }

        return subset;
    }
}
