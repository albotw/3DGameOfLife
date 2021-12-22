package fr.albot.GameOfLife.core;

import java.io.Serializable;
import java.util.*;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ALIVE_THRESHOLD;
import static fr.albot.GameOfLife.CONFIG.CONFIG.CURRENT_THRESHOLD;

public class GenerateProcess implements IGOLProcess, Serializable {

    private HashSet<Integer> alive;
    private Map<Integer, Integer> neighbours;
    private ArrayList<Integer> toCreate;


    public GenerateProcess(HashSet<Integer> alive, Map<Integer, Integer> neighbours) {
        this.alive = alive;
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        System.out.println(Arrays.toString(neighbours.values().toArray()));
        toCreate = new ArrayList<>();

        for (Integer cell : neighbours.keySet()) {
            Integer aliveNeighbours = neighbours.get(cell);
            if ((aliveNeighbours == ALIVE_THRESHOLD || aliveNeighbours == CURRENT_THRESHOLD) && !alive.contains(cell)) {
                toCreate.add(cell);
            }
        }

        System.out.println("Cells to create: " + toCreate.size());
    }

    public HashSet<Integer> getNextAlive() {
        return this.alive;
    }

    public HashMap<Integer, Integer> getNextNeighbours() {
        return (HashMap<Integer, Integer>) this.neighbours;
    }

    @Override
    public ArrayList<Integer> getResult() {
        return this.toCreate;
    }
}
