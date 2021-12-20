package fr.albot.GameOfLife.core;

import java.util.HashMap;
import java.util.HashSet;

import static fr.albot.GameOfLife.CONFIG.CONFIG.*;

public class PurgeProcess implements IGOLProcess {
    private HashSet<Integer> alive; // sous ensemble de cellules en vies
    private HashMap<Integer, Integer> neighbours; //ensemble complet du voisinage

    private HashSet<Integer> next_alive;
    private HashMap<Integer, Integer> next_neighbours;

    public PurgeProcess(HashSet<Integer> alive, HashMap<Integer, Integer> neighbours) {
        this.alive = alive;
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        this.next_alive = new HashSet<Integer> (alive);
        this.next_neighbours = new HashMap<Integer, Integer> (this.neighbours);
        for (int cell : alive) {
            Integer n = neighbours.get(cell);
            if (n == null || n < CURRENT_THRESHOLD || n > ALIVE_THRESHOLD) {
                next_alive.remove(cell);

                for (int i = cell - 13; i < cell + 13; i++) {
                    if (i != cell && i >= 0 && i < ENV_LENGTH) {
                        next_neighbours.put(cell, next_neighbours.get(cell) - 1);
                    }
                }
            }
        }
    }

    @Override
    public HashSet<Integer> getNextAlive() {
        return this.next_alive;
    }

    @Override
    public HashMap<Integer, Integer> getNextNeighbours() {
        return this.next_neighbours;
    }
}
