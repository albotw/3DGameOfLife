package fr.albot.GameOfLife.core;

public enum Cell {
    Alive(1),
    Empty(0);

    int value;

    Cell(int value) {
        this.value = value;
    }
}
