package fr.albot.GameOfLife.core.distributed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public interface IGOLProcess {
    void run();

    HashSet<Integer> getNextAlive();

    ConcurrentHashMap<Integer, Integer> getNextNeighbours();

    ArrayList<Integer> getResult();
}
