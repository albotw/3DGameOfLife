package fr.albot.GameOfLife.Engine.geometry;

public class Cube extends Sprite {

    public Cube(boolean wireframe) {
        super(null, wireframe);
    }

    @Override
    public void purge() {
        this.position = null;
        this.rotation = null;
    }
}
