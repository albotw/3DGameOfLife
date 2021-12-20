package fr.albot.GameOfLife.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GenerateProcess implements IGOLProcess, Serializable {
    private Map<Integer, Integer> precast;

    private HashSet<Integer> alive;
    private HashMap<Integer, Integer> neighbours;

    private HashSet<Integer> next_alive;
    private HashMap<Integer, Integer> next_neighbours;

    public GenerateProcess(HashSet<Integer> alive, Map<Integer, Integer> precast) {
        this.alive = alive;
        this.precast = precast;
    }

    @Override
    public void run() {
        this.next_alive = new HashSet<Integer>(alive);
        this.next_neighbours = new HashMap<Integer, Integer>(neighbours);

        for (Integer cell : neighbours.keySet()) {
            Integer aliveNeighbours = neighbours.get(cell);
            if (aliveNeighbours == 3 && !alive.contains(cell)) {
                next_alive.add(cell);

                //TODO: rendre les constantes configurables.
                for (int i = cell - 13; i < cell + 13; i++) {
                    if (i != cell) {
                        next_neighbours.put(cell, next_neighbours.get(cell) + 1);
                    }
                }
            }
        }
    }

    public HashSet<Integer> getNext_alive() {
        return this.next_alive;
    }

    public HashMap<Integer, Integer> getNext_neighbours() {
        return this.next_neighbours;
    }
}
