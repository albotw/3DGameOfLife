package fr.albot.GameOfLife.core;

import java.util.HashMap;
import java.util.HashSet;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ALIVE_THRESHOLD;
import static fr.albot.GameOfLife.CONFIG.CONFIG.CURRENT_THRESHOLD;

public class PurgeProcess implements IGOLProcess {
    private HashSet<Integer> alive; // sous ensemble de cellules en vies
    private HashMap<Integer, Integer> neighbours; //ensemble complet du voisinage

    public PurgeProcess(HashSet<Integer> alive, HashMap<Integer, Integer> neighbours) {
        this.alive = alive;
        this.neighbours = neighbours;
    }

    @Override
    public void run() {
        for (int cell : alive) {
            Integer n = neighbours.get(cell);
            if (n == null || n < CURRENT_THRESHOLD || n > ALIVE_THRESHOLD) {
                alive.remove(cell);
                //TODO: décrémenter tous ses voisins.
            }
        }
    }
}
