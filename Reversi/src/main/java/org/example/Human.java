package org.example;

import java.util.Scanner;

public class Human extends Player {

    private final Scanner inputStream = new Scanner(System.in);

    public Human(final String name, final String color) {
        super(name, color);
    }

    private static int tryParseInt(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected Move getMove(final Board board, final int countOfMoves) {
        int move = -1;
        while (move < 0 || move > countOfMoves) {
            System.out.print("\nEnter move: ");
            move = tryParseInt(inputStream.next());
            if (move == 0 && board.getCountOfMoves() == countOfMoves) {
                move = -1;
            }
            if (move == -1) {
                System.out.println("Invalid move");
            }
        }
        if (board.getCountOfMoves() < countOfMoves && move == 0) {
            return new Move(-1, -1, 0);
        } else {
            return board.getMove(move);
        }
    }
}
