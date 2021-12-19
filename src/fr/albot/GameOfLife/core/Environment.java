package fr.albot.GameOfLife.core;

import java.util.Arrays;

import static fr.albot.GameOfLife.CONFIG.CONFIG.ENV_LENGTH;
import static fr.albot.GameOfLife.CONFIG.CONFIG.ENV_SIZE;

public class Environment {

    private boolean[] current_env;
    private boolean[] future_env;

    public Environment() {
        this.current_env = new boolean[ENV_LENGTH];
        this.future_env = new boolean[ENV_LENGTH];

        this.clean(current_env);
        this.clean(future_env);
    }

    public static int to1d(int x, int y, int z) {
        return (x + (y * ENV_SIZE) + z * (ENV_SIZE * ENV_SIZE));
    }

    public void randomValues(int quantity) {
        int counter = 0;
        do {
            int pos = (int) (Math.random() * ENV_LENGTH);

            if (!this.current_env[pos]) {
                this.current_env[pos] = true;
                //System.out.println("added cell at " + x + " " + y + " " + z);
                counter++;
            }
        } while (counter < quantity);
    }

    public int size() {
        return this.current_env.length;
    }

    private void clean(boolean[] env) {
        Arrays.fill(env, false);
    }

    public void purge() {
        this.current_env = null;
        this.future_env = null;
    }

    public boolean[] getSubEnv(int pos) {
        //TODO: calculer la valeur dynamique.
        //27 valeurs dans un cube de 3 * 3 * 3. pos est le point milieu.
        int begin = pos - 13;
        if (begin < 0) begin = 0;

        int end = pos + 13;
        return Arrays.copyOfRange(current_env, begin, end);
    }

    public boolean getCellState(int x, int y, int z) {
        if (x >= 0 && x < ENV_SIZE && y >= 0 && y < ENV_SIZE && z >= 0 && z < ENV_SIZE) {
            return this.current_env[to1d(x, y, z)];
        } else return false;
    }

    public void setCellState(int pos, boolean cell) {
        if (pos >= 0 && pos < ENV_LENGTH) {
            this.future_env[pos] = cell;
        }
    }

    public void nextGeneration() {
        System.arraycopy(future_env, 0, current_env, 0, future_env.length);
        clean(this.future_env);
    }
}
