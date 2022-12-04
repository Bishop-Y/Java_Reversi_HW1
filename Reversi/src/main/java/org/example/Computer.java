package org.example;

import java.util.ArrayList;

public class Computer extends Player {
    public Computer(final String name, final String color) {
        super(name, color);
    }

    @Override
    protected Move getMove(final Board board, final int getCountOfMoves) {
        ArrayList<Move> movesList = board.getCopyOfMoves();
        double max = 0.0;
        for (Move move : movesList) {
            System.out.println(move);
            max = Math.max(max, move.value());
        }
        int index = 0;
        for (int i = 0; i < movesList.size(); i++) {
            if (movesList.get(i).value() == max) {
                index = i;
                break;
            }
        }
        return board.getMove(index + 1);
    }
}
