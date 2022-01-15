package fr.albot.GameOfLife.core.distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ALIVE_THRESHOLD;
import static fr.albot.GameOfLife.CONFIG.CONFIG.CURRENT_THRESHOLD;

public class PurgeProcess implements IGOLProcess, Serializable {
    private HashSet<Integer> alive; // sous ensemble de cellules en vies
    private ConcurrentHashMap<Integer, Integer> neighbours; //ensemble complet du voisinage
    private ArrayList<Integer> toDelete;


    public PurgeProcess(HashSet<Integer> alive, ConcurrentHashMap<Integer, Integer> neighbours) {
        this.alive = alive;
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        System.out.println("Starting purge subprocess");
        //System.out.println(Arrays.toString(neighbours.values().toArray()));
        this.toDelete = new ArrayList<>();
        for (int cell : alive) {
            Integer n = neighbours.get(cell);
            if (n == null || n < CURRENT_THRESHOLD || n > ALIVE_THRESHOLD) {
                toDelete.add(cell);
            }
        }
        System.out.println("Cells to delete: " + toDelete.size());
        System.out.println("## Process duration: " + (System.currentTimeMillis() - before) + " ms");
    }

    @Override
    public HashSet<Integer> getNextAlive() {
        return this.alive;
    }

    @Override
    public ConcurrentHashMap<Integer, Integer> getNextNeighbours() {
        return this.neighbours;
    }

    @Override
    public ArrayList<Integer> getResult() {
        return this.toDelete;
    }
}
