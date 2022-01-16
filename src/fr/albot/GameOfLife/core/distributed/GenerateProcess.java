package fr.albot.GameOfLife.core.distributed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ALIVE_THRESHOLD;
import static fr.albot.GameOfLife.CONFIG.CONFIG.CURRENT_THRESHOLD;

public class GenerateProcess implements IGOLProcess, Serializable {

    private HashSet<Integer> alive;
    private ConcurrentHashMap<Integer, Integer> neighbours;
    private ArrayList<Integer> toCreate;


    public GenerateProcess(HashSet<Integer> alive, ConcurrentHashMap<Integer, Integer> neighbours) {
        this.alive = alive;
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        long before = System.currentTimeMillis();
        System.out.println("Starting generate subprocess");
        //System.out.println(Arrays.toString(neighbours.values().toArray()));
        toCreate = new ArrayList<>();

        for (Integer cell : neighbours.keySet()) {
            Integer aliveNeighbours = neighbours.get(cell);
            if ((aliveNeighbours == ALIVE_THRESHOLD || aliveNeighbours == CURRENT_THRESHOLD) && !alive.contains(cell)) {
                toCreate.add(cell);
            }
        }

        System.out.println("Cells to create: " + toCreate.size());
        System.out.println("## Process duration: " + (System.currentTimeMillis() - before) + " ms");
    }

    public HashSet<Integer> getNextAlive() {
        return this.alive;
    }

    public ConcurrentHashMap<Integer, Integer> getNextNeighbours() {
        return (ConcurrentHashMap<Integer, Integer>) this.neighbours;
    }

    @Override
    public ArrayList<Integer> getResult() {
        return this.toCreate;
    }
}
