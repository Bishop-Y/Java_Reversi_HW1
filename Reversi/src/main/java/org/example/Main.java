package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Player player1, player2;
    private static Board board;
    private static ArrayList<Board> oldBoards;
    private static int turn;

    public static boolean isGameOver() {
        if (turn == 0) {
            return !board.hasValidMove(player1.cell);
        } else {
            return !board.hasValidMove(player2.cell);
        }
    }

    public static void main(String[] args) {
        String type, name;
        Scanner scan = new Scanner(System.in);

        do {
            System.out.println("""
                    Choose a mode of game:
                    \t 1. Player vs player.
                    \t 2. Player vs computer.
                    \t 3. Computer vs computer.
                    To exit enter any key.""");

            if (player1 instanceof Human) {
                System.out.println("\nPlayer " + player1.name + " has " + player1.score + " max points for this session.");
            }
            if (player2 instanceof Human) {
                System.out.println("Player " + player2.name + " has " + player2.score + " max points for this session.");
            }

            System.out.print("\nType a mode: ");
            type = scan.next();

            switch (type) {
                case "1" -> {
                    System.out.println("\nFirst player will be white and will go first.");
                    if (player1 == null || player1 instanceof Computer) {
                        System.out.print("First player, type your name: ");
                        name = scan.next();
                        player1 = new Human(name, "White");
                    }
                    if (player2 == null || player2 instanceof Computer) {
                        System.out.print("Second player, type your name: ");
                        name = scan.next();
                        player2 = new Human(name, "Black");
                    }
                }
                case "2" -> {
                    System.out.println("\nYou will play with computer, your color will be white. " +
                            "\nThe first one who walks is determined randomly.");
                    if (player1 == null || player1 instanceof Computer) {
                        System.out.print("Type your name: ");
                        name = scan.next();
                        player1 = new Human(name, "White");
                    }
                    player2 = new Computer("Bishop", "Black");
                }
                case "3" -> {
                    player1 = new Computer("Bishop", "Black");
                    player2 = new Computer("Karai", "White");
                }
                default -> {
                    return;
                }
            }
            board = new Board();
            oldBoards = new ArrayList<Board>();
            if (type.equals("1")) {
                turn = 0;
            } else {
                turn = (int) (Math.random() * 2);
            }
            while (!isGameOver()) {
                Player currentPlayer = turn == 0 ? player1 : player2;
                board.hasValidMove(currentPlayer.cell);
                board.displayBoard(turn, oldBoards.size());
                System.out.println("Player " + player1.name + " has " + board.getPlayerScore(player1.cell) + " points" +
                        "\nPlayer " + player2.name + " has " + board.getPlayerScore(player2.cell) + " points" +
                        "\nIt is " + currentPlayer.name + " turn (" + currentPlayer.cell.toString() + ")");
                takeTurn(currentPlayer);
            }

            int pointsOfFirst = board.getPlayerScore(player1.cell);
            int pointsOfSecond = board.getPlayerScore(player2.cell);
            player1.score = Math.max(pointsOfFirst, player1.score);
            player2.score = Math.max(pointsOfSecond, player2.score);
            board.displayBoard(-1, 0);
            System.out.println("\nGame over!");

            if (pointsOfFirst > pointsOfSecond) {
                System.out.println("Player " + player1.name + " won this round.\n");
            } else if (pointsOfFirst < pointsOfSecond) {
                System.out.println("Player " + player2.name + " won this round.\n");
            } else {
                System.out.println("Friendship won this round\n");
            }
        } while (true);
    }

    private static void takeTurn(Player player) {
        Move move;
        if ((turn % 2 == 0 && oldBoards.size() >= 2) || (turn % 2 == 1 && oldBoards.size() >= 3)) {
            move = player.getMove(board, board.getCountOfMoves() + 1);
        } else {
            move = player.getMove(board, board.getCountOfMoves());
        }
        if (move.row() == -1 && move.column() == -1) {
            board = oldBoards.get(oldBoards.size() - 2);
            oldBoards.remove(oldBoards.size() - 1);
            oldBoards.remove(oldBoards.size() - 1);
            return;
        }
        oldBoards.add(new Board(board));
        board.makeMove(player.cell, move.row(), move.column());
        board.clearMoves();
        turn += 1;
        turn %= 2;
    }
}