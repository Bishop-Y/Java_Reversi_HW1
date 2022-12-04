package org.example;

import java.util.ArrayList;

public class Board {
    private final int size;
    private final Cell[][] board;

    private final Cell[][] movesBoard;

    private final ArrayList<Move> moves;

    public Board() {
        size = 8;
        board = new Cell[size][size];
        movesBoard = new Cell[size][size];
        moves = new ArrayList<Move>();
        board[size / 2 - 1][size / 2 - 1] = new Cell('●');
        board[size / 2 - 1][size / 2] = new Cell('○');
        board[size / 2][size / 2 - 1] = new Cell('○');
        board[size / 2][size / 2] = new Cell('●');
    }

    public Board(Board other) {
        this.size = other.size;
        board = new Cell[size][size];
        movesBoard = new Cell[size][size];
        moves = new ArrayList<Move>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (other.board[row][col] != null) {
                    this.board[row][col] = new Cell(other.board[row][col]);
                }
                if (other.movesBoard[row][col] != null) {
                    this.movesBoard[row][col] = new Cell(other.movesBoard[row][col]);
                }
            }
        }
        moves.addAll(other.moves);
    }

    public int getSize() {
        return size;
    }

    public int getCountOfMoves() {
        return moves.size();
    }

    public Move getMove(int number) {
        number -= 1;
        return moves.get(number);
    }

    public ArrayList<Move> getCopyOfMoves() {
        return new ArrayList<Move>(moves);
    }

    public void clearMoves() {
        moves.clear();
    }

    public int getPlayerScore(final Cell cell) {
        int count = 0;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != null && board[row][col].equals(cell)) {
                    count++;
                }
            }
        }

        return count;
    }

    public boolean isValidMove(final Cell cell, final int row, final int col) {

        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        if (board[row][col] != null) {
            return false;
        }

        return lookRight(cell, row, col) ||
                lookDown(cell, row, col) ||
                lookLeft(cell, row, col) ||
                lookUp(cell, row, col) ||
                lookUpLeft(cell, row, col) ||
                lookUpRight(cell, row, col) ||
                lookDownRight(cell, row, col) ||
                lookDownLeft(cell, row, col);
    }

    public boolean hasValidMove(final Cell cell) {
        var flag = false;
        double value;
        moves.clear();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (isValidMove(cell, row, col)) {
                    movesBoard[row][col] = new Cell('?');
                    value = getAllScores(cell, row, col);
                    moves.add(new Move(row, col, value));
                    flag = true;
                }
            }
        }
        return flag;
    }

    private boolean isOpponentCell(final Cell cell, final int row, final int col) {
        return board[row][col] != null && !board[row][col].equals(cell);
    }



    public double getScoresForCell(int row, int col) {
        if (row == 0 || row == (size - 1) || col == 0 || col == (size - 1)) {
            return 2.0;
        } else {
            return 1.0;
        }
    }

    public double getAllScores(final Cell cell, final int row, final int col) {
        double value = 1.0;

        if (row == 0 || row == (size - 1) || col == 0 || col == (size - 1)) {
            value = 2.4;
        }
        if (row + col == 0 || row + col == 2 * (size - 1) || (row == 0 && col == (size - 1)) || (row == (size - 1) && col == 0)) {
            value = 2.8;
        }

        int next;
        if (lookDown(cell, row, col)) {
            next = row + 1;
            while (next < size && isOpponentCell(cell, next, col)) {
                value += getScoresForCell(next, col);
                next++;
            }
        }

        if (lookRight(cell, row, col)) {
            next = col + 1;
            while (next < size && isOpponentCell(cell, row, next)) {
                value += getScoresForCell(row, next);
                next++;
            }
        }

        if (lookUp(cell, row, col)) {
            next = row - 1;
            while (next >= 0 && isOpponentCell(cell, next, col)) {
                value += getScoresForCell(next, col);
                next--;
            }
        }

        if (lookLeft(cell, row, col)) {
            next = col - 1;
            while (next >= 0 && isOpponentCell(cell, row, next)) {
                value += getScoresForCell(row, next);
                next--;
            }
        }

        int nextRow, nextCol;
        if (lookUpLeft(cell, row, col)) {
            nextRow = row - 1;
            nextCol = col - 1;
            while (nextRow >= 0 && nextCol >= 0 && isOpponentCell(cell, nextRow, nextCol)) {
                value += getScoresForCell(nextRow, nextCol);
                nextRow--;
                nextCol--;
            }
        }

        if (lookUpRight(cell, row, col)) {
            nextRow = row - 1;
            nextCol = col + 1;
            while (nextRow >= 0 && nextCol < size && isOpponentCell(cell, nextRow, nextCol)) {
                value += getScoresForCell(nextRow, nextCol);
                nextRow--;
                nextCol++;
            }
        }

        if (lookDownLeft(cell, row, col)) {
            nextRow = row + 1;
            nextCol = col - 1;
            while (nextRow < size && nextCol >= 0 && isOpponentCell(cell, nextRow, nextCol)) {
                value += getScoresForCell(nextRow, nextCol);
                nextRow++;
                nextCol--;
            }
        }

        if (lookDownRight(cell, row, col)) {
            nextRow = row + 1;
            nextCol = col + 1;
            while (nextRow < size && nextCol < size && isOpponentCell(cell, nextRow, nextCol)) {
                value += getScoresForCell(nextRow, nextCol);
                nextRow++;
                nextCol++;
            }
        }
        return value;
    }

    public void makeMove(final Cell cell, final int row, final int col) {
        board[row][col] = cell;

        int next;
        if (lookDown(cell, row, col)) {
            next = row + 1;
            while (next < size && isOpponentCell(cell, next, col)) {
                board[next][col] = cell;
                next++;
            }
        }

        if (lookRight(cell, row, col)) {
            next = col + 1;
            while (next < size && isOpponentCell(cell, row, next)) {
                board[row][next] = cell;
                next++;
            }
        }

        if (lookUp(cell, row, col)) {
            next = row - 1;
            while (next >= 0 && isOpponentCell(cell, next, col)) {
                board[next][col] = cell;
                next--;
            }
        }

        if (lookLeft(cell, row, col)) {
            next = col - 1;
            while (next >= 0 && isOpponentCell(cell, row, next)) {
                board[row][next] = cell;
                next--;
            }
        }

        int nextRow, nextCol;
        if (lookUpLeft(cell, row, col)) {
            nextRow = row - 1;
            nextCol = col - 1;
            while (nextRow >= 0 && nextCol >= 0 && isOpponentCell(cell, nextRow, nextCol)) {
                board[nextRow][nextCol] = cell;
                nextRow--;
                nextCol--;
            }
        }

        if (lookUpRight(cell, row, col)) {
            nextRow = row - 1;
            nextCol = col + 1;
            while (nextRow >= 0 && nextCol < size && isOpponentCell(cell, nextRow, nextCol)) {
                board[nextRow][nextCol] = cell;
                nextRow--;
                nextCol++;
            }
        }

        if (lookDownLeft(cell, row, col)) {
            nextRow = row + 1;
            nextCol = col - 1;
            while (nextRow < size && nextCol >= 0 && isOpponentCell(cell, nextRow, nextCol)) {
                board[nextRow][nextCol] = cell;
                nextRow++;
                nextCol--;
            }
        }

        if (lookDownRight(cell, row, col)) {
            nextRow = row + 1;
            nextCol = col + 1;
            while (nextRow < size && nextCol < size && isOpponentCell(cell, nextRow, nextCol)) {
                board[nextRow][nextCol] = cell;
                nextRow++;
                nextCol++;
            }
        }
    }

    private boolean lookDown(final Cell cell, final int row, final int col) {
        int next = row + 1;
        if (row + 1 < size && isOpponentCell(cell, next, col)) {
            next++;
            while (next < size) {
                if (board[next][col] == null) {
                    break;
                } else if (board[next][col].equals(cell)) {
                    return true;
                }
                next++;
            }
        }
        return false;
    }

    private boolean lookRight(final Cell cell, final int row, final int col) {
        int next = col + 1;
        if (col + 1 < size && isOpponentCell(cell, row, next)) {
            next++;
            while (next < size) {
                if (board[row][next] == null) {
                    break;
                } else if (board[row][next].equals(cell)) {
                    return true;
                }
                next++;
            }
        }
        return false;
    }

    private boolean lookUp(final Cell cell, final int row, final int col) {
        int next = row - 1;
        if (next >= 0 && isOpponentCell(cell, next, col)) {
            next--;
            while (next >= 0) {
                if (board[next][col] == null) {
                    break;
                } else if (board[next][col].equals(cell)) {
                    return true;
                }
                next--;
            }
        }
        return false;
    }

    private boolean lookLeft(final Cell cell, final int row, final int col) {
        int next = col - 1;
        if (next >= 0 && isOpponentCell(cell, row, next)) {
            next--;
            while (next >= 0) {
                if (board[row][next] == null) {
                    break;
                } else if (board[row][next].equals(cell)) {
                    return true;
                }
                next--;
            }
        }
        return false;
    }

    private boolean lookDownRight(final Cell cell, final int row, final int col) {
        int nextRow = row + 1;
        int nextCol = col + 1;
        if (nextRow < size && nextCol < size && isOpponentCell(cell, nextRow, nextCol)) {
            nextRow++;
            nextCol++;
            while (nextRow < size && nextCol < size) {
                if (board[nextRow][nextCol] == null) {
                    break;
                } else if (board[nextRow][nextCol].equals(cell)) {
                    return true;
                }
                nextRow++;
                nextCol++;
            }
        }
        return false;
    }

    private boolean lookUpLeft(final Cell cell, final int row, final int col) {
        int nextRow = row - 1;
        int nextCol = col - 1;
        if (nextRow >= 0 && nextCol >= 0 && isOpponentCell(cell, nextRow, nextCol)) {
            nextRow--;
            nextCol--;
            while (nextRow >= 0 && nextCol >= 0) {
                if (board[nextRow][nextCol] == null) {
                    break;
                } else if (board[nextRow][nextCol].equals(cell)) {
                    return true;
                }
                nextRow--;
                nextCol--;
            }
        }
        return false;
    }

    private boolean lookUpRight(final Cell cell, final int row, final int col) {
        int nextRow = row - 1;
        int nextCol = col + 1;
        if (nextRow >= 0 && nextCol < size && isOpponentCell(cell, nextRow, nextCol)) {
            nextRow--;
            nextCol++;
            while (nextRow >= 0 && nextCol < size) {
                if (board[nextRow][nextCol] == null) {
                    break;
                } else if (board[nextRow][nextCol].equals(cell)) {
                    return true;
                }
                nextRow--;
                nextCol++;
            }
        }
        return false;
    }

    private boolean lookDownLeft(final Cell cell, final int row, final int col) {
        int nextRow = row + 1;
        int nextCol = col - 1;
        if (nextRow < size && nextCol >= 0 && isOpponentCell(cell, nextRow, nextCol)) {
            nextRow++;
            nextCol--;
            while (nextRow < size && nextCol >= 0) {
                if (board[nextRow][nextCol] == null) {
                    break;
                } else if (board[nextRow][nextCol].equals(cell)) {
                    return true;
                }
                nextRow++;
                nextCol--;
            }
        }
        return false;
    }

    public void displayBoard(int turn, int boards) {
        System.out.print("\n\n\n\n ");
        for (int i = 0; i < size; i++) {
            System.out.printf("   %d", i + 1);
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print("  +");
            for (int j = 0; j < size; j++) {
                System.out.print("---+");
            }
            System.out.printf("\n%d |", i + 1);
            for (int j = 0; j < size; j++) {
                if (board[i][j] != null) {
                    System.out.printf(" %s |", board[i][j]);
                } else {
                    if (movesBoard[i][j] != null) {
                        System.out.printf(" %s |", movesBoard[i][j]);
                    } else {
                        System.out.print("   |");
                    }
                }
            }
            System.out.println();
        }
        System.out.print("  +");
        for (int j = 0; j < size; j++) {
            System.out.print("---+");
        }
        System.out.println();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (movesBoard[row][col] != null) {
                    movesBoard[row][col] = null;
                }
            }
        }
        if (turn != -1) {
            StringBuilder text = new StringBuilder("Available moves: ");
            if ((turn % 2 == 0 && boards >= 2) || (turn % 2 == 1 && boards >= 3)) {
                text.append("0. Cancel move  ");
            }
            for (int i = 0; i < moves.size(); i++) {
                text.append(i + 1).append(". (").append(moves.get(i).row() + 1).append(",").append(moves.get(i).column() + 1).append(")  ");
            }
            System.out.println(text);
        }
    }
}
