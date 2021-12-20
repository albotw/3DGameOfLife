package fr.albot.GameOfLife.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public interface IGOLProcess {
    void run();

    HashSet<Integer> getNextAlive();
    HashMap<Integer, Integer> getNextNeighbours();
}
