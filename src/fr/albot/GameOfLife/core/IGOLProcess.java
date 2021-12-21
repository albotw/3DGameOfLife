package fr.albot.GameOfLife.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface IGOLProcess {
    void run();

    HashSet<Integer> getNextAlive();

    HashMap<Integer, Integer> getNextNeighbours();

    ArrayList<Integer> getResult();
}
