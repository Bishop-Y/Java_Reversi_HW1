package org.example;

import java.util.Objects;

public abstract class Player {

    protected final String name;
    protected final Cell cell;

    protected int score = 0;

    public Player (final String name, final String color) {
        this.name = name;
        this.cell = Objects.equals(color, "Black") ? new Cell('○') : new Cell('●');
    }

    abstract protected Move getMove(final Board board, final int getCountOfMoves);
}
