package fr.albot.GameOfLife.Engine.events.Events;

import fr.albot.GameOfLife.Engine.events.Event;

import java.util.HashSet;

public class SetBaseEnv extends Event {
    public HashSet<Integer> alive;

    public SetBaseEnv(HashSet<Integer> alive) {
        this.alive = alive;
    }
}
