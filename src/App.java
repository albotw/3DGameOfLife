import core.Cell;
import core.Environment;
import graphics.Mesh;
import graphics.Renderer;

import static CONFIG.CONFIG.ENV_SIZE;

public class App {
    public static Environment env;
    public static void main(String[] args)
    {
        //System.out.println("mode: " + args[0]);
        //System.out.println("threads: " + args[1]);
        System.out.println("Hello, World !");

        env = new Environment();

        init();
        loop();

        Renderer r = new Renderer();
        r.start();
    }

    public static void init()
    {
        //ajout cellules initiales
        int[] baseConfig = {
                1, 0,
                1, 1,
                1, 2
        };
        env.initValues(baseConfig);
    }

    public static int getAliveNeighbours(int i, int j)
    {
        int counter = 0;
        if (env.getCellState(i - 1, j - 1) == Cell.Alive) counter++;   //haut a gauche
        if (env.getCellState(i - 1, j) == Cell.Alive) counter++;          //haut milieu
        if (env.getCellState(i - 1, j + 1) == Cell.Alive) counter++;   //haut a droite
        if (env.getCellState(i, j - 1) == Cell.Alive) counter++;          //milieu gauche
        if (env.getCellState(i, j + 1) == Cell.Alive) counter++;          //milieu droite
        if (env.getCellState(i + 1, j - 1) == Cell.Alive) counter++;   //bas a gauche
        if (env.getCellState(i + 1, j) == Cell.Alive) counter++;          //bas milieu
        if (env.getCellState(i + 1, j + 1) == Cell.Alive) counter++;   //bas a droite

        return counter;
    }

    public static void loop()
    {
        //cycles
        for (int cycle = 0; cycle < 3; cycle++)
        {
            //pour chaque cellule
            for (int i = 0; i < ENV_SIZE; i++)
            {
                for (int j = 0; j < ENV_SIZE; j++)
                {
                    int aliveNeighbours = getAliveNeighbours(i, j);
                    if (aliveNeighbours == 3) {
                        env.setCellState(i, j, Cell.Alive);
                    }
                    else if (aliveNeighbours == 2)
                    {
                        env.setCellState(i, j, env.getCellState(i, j));
                    }
                    else if (aliveNeighbours < 2 || aliveNeighbours > 3) {
                        env.setCellState(i, j, Cell.Empty);
                    }
                }
            }
            env.print();
            env.nextGeneration();
        }
        env.print();
    }
}
