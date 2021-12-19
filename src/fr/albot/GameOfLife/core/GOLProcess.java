package fr.albot.GameOfLife.core;

import java.io.Serializable;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ALIVE_THRESHOLD;
import static fr.albot.GameOfLife.CONFIG.CONFIG.CURRENT_THRESHOLD;

public class GOLProcess implements IGOLProcess, Serializable {
    private final boolean[] local_env;
    public final int pos;
    private boolean result;

    public GOLProcess(int pos, boolean[] local_env) {
        this.pos = pos;
        this.local_env = local_env;
    }

    public boolean updatedState() {
        return this.result;
    }

    public void run() {
        int aliveNeighbours = getAliveNeighbours();
        //System.out.println(this.x + " " + this.y + " " + this.z + " " + aliveNeighbours);
        if (aliveNeighbours == ALIVE_THRESHOLD) { //naissance
            this.result = true;
        } else if (aliveNeighbours == CURRENT_THRESHOLD) { //état courant
            this.result = local_env[14];
        } else { //mort
            this.result = false;
        }
    }


    //TODO: optimisation ?
    private int getAliveNeighbours() {
        int counter = 0;
        for (int i = 0; i < local_env.length; i++) {
            if (i != 14) //valeur milieu d'un cube 1d de 3 de coté.
                if (local_env[i]) counter++;
        }
        return counter;
    }
}
