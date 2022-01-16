package fr.albot.GameOfLife.core;

import fr.albot.GameOfLife.CONFIG.CONFIG;
import fr.albot.GameOfLife.Engine.SpriteManager;
import fr.albot.GameOfLife.Engine.events.EventQueue;
import fr.albot.GameOfLife.Engine.events.Events.SetBaseEnv;
import fr.albot.GameOfLife.Engine.events.ThreadID;

import java.util.ArrayList;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ALIVE_THRESHOLD;
import static fr.albot.GameOfLife.CONFIG.CONFIG.CURRENT_THRESHOLD;

public class GameOfLife {
    //informations
    private long duration = 0;

    private ArrayList<Integer> toDelete;
    private ArrayList<Integer> toCreate;
    private Environment env;

    private EventQueue eventQueue;

    private int currentThreshold;
    private int aliveThreshold;

    public GameOfLife() {
        this.eventQueue = new EventQueue(ThreadID.GOL);
        this.toDelete = new ArrayList<Integer>();
        this.toCreate = new ArrayList<Integer>();
        this.env = new Environment();
    }

    public void init() {
        this.currentThreshold = CURRENT_THRESHOLD;
        this.aliveThreshold = ALIVE_THRESHOLD;

        if (CONFIG.PATTERN == Pattern.CUSTOM) {
            this.env.loadFromFile(CONFIG.PATTERN_FILE);
        } else if (CONFIG.PATTERN == Pattern.RAND) {
            this.env.randomValues();
        }

        this.eventQueue.send(new SetBaseEnv(this.env.getAlive()), ThreadID.Render);
    }

    public void purge() {
        this.env.purge();
        this.toDelete.clear();
        this.toCreate.clear();
    }

    public void step() {
        long before_step = System.currentTimeMillis();
        toCreate.clear();
        toDelete.clear();

        for (int cell : env.getAlive()) {
            Integer n = env.getNeighbours().get(cell);
            if (n == null || n < this.currentThreshold || n > this.aliveThreshold) {
                toDelete.add(cell);
            }
        }

        for (int cell : env.getNeighbours().keySet()) {
            Integer aliveNeighbours = env.getNeighbours().get(cell);
            if ((aliveNeighbours == this.aliveThreshold || aliveNeighbours == this.currentThreshold) && !env.getAlive().contains(cell)) {
                toCreate.add(cell);
            }
        }

        env.nextGeneration(toDelete, toCreate);
        this.duration = System.currentTimeMillis() - before_step;
        SpriteManager.instance.update(this.env.getAlive());
    }

    public long getDuration() {
        return this.duration;
    }

    public int getAliveSize() {
        return this.env.getAliveSize();
    }

    public int getNeighboursSize() {
        return this.env.getNeighboursSize();
    }
}
