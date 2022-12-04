package org.example;

public class Cell {
    public char color;

    public Cell(char color) {
        this.color = color;
    }

    public Cell(Cell other) {
        this(other.color);
    }

    @Override
    public String toString() {
        return String.valueOf(color);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Cell))  {
            return false;
        }

        return color == ((Cell) obj).color;
    }
}
