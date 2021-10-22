package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

import java.util.Arrays;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;


    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();

    }

    private void drawScene() {
        for (int x = 0; x < gameField.length; x++) {
            for (int y = 0; y < gameField[x].length; y++) {
                setCellColoredNumber(y, x, gameField[x][y]);
            }
        }
    }

    private void createNewNumber() {
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        int n = getRandomNumber(10) == 1 ? 4 : 2;
        while (gameField[x][y] != 0) {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        }
        gameField[x][y] = n;
         if (getMaxTileValue() == 2048) win();

    }

    private Color getColorByValue(int value) {
        if (value == 0) return Color.WHEAT;
        if (value == 2) return Color.BLUE;
        if (value == 4) return Color.YELLOW;
        if (value == 8) return Color.ALICEBLUE;
        if (value == 16) return Color.AQUA;
        if (value == 32) return Color.ANTIQUEWHITE;
        if (value == 64) return Color.AZURE;
        if (value == 128) return Color.BEIGE;
        if (value == 256) return Color.BISQUE;
        if (value == 512) return Color.AQUAMARINE;
        if (value == 1024) return Color.BLANCHEDALMOND;
        if (value == 2048) return Color.BROWN;

        return null;
    }

    private void setCellColoredNumber(int x, int y, int color) {
        setCellValueEx(x, y, getColorByValue(color), color > 0 ? color + "" : "");
    }

    private boolean compressRow(int[] row) {
        boolean n = false;
        int number;
        for (int t = 0; t < row.length; ) {
            if (t < row.length - 1 && row[t] == 0 && row[t + 1] > 0) {
                number = row[t];
                row[t] = row[t + 1];
                row[t + 1] = number;
                t = 0;
                n = true;
            } else t++;
        }
        return n;
    }

    private boolean mergeRow(int[] row) {
        boolean n = false;
        for (int t = 0; t < row.length; ) {
            if (t < row.length - 1 && row[t] != 0 && row[t] == row[t + 1]) {
                row[t] += row[t + 1];
                row[t + 1] = 0;
                score+=row[t];
                setScore(score);
                t++;
                n = true;

            } else t++;
        }
        return n;
    }

    private void moveLeft() {
        boolean change = false;
        for (int i = 0; i < gameField.length; i++) {
            if (compressRow(gameField[i])) change = true;
            if (mergeRow(gameField[i])) change = true;
            if (compressRow(gameField[i])) change = true;
        }
        if (change) createNewNumber();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] copy = new int[SIDE][SIDE];
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                copy[y][SIDE - x - 1] = gameField[x][y];
            }
        }
        gameField = copy;
    }

    private int getMaxTileValue() {
        int max = 0;

        for (int[] ints : gameField) {
            for (int anInt : ints) {
                max = Math.max(max, anInt);
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHEAT, "VICTORY", Color.BLACK, 24);
    }

    private boolean canUserMove(){
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0) {
                    return true;
                } else if (y < SIDE - 1 && gameField[y][x] == gameField[y + 1][x]) {
                    return true;
                } else if ((x < SIDE - 1) && gameField[y][x] == gameField[y][x + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHEAT, "GAME OVER", Color.BLACK, 24);
    }


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove()){
            gameOver();
            return;
        }
        if (isGameStopped) {
            if (key == key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            } else return;
        }
        switch (key) {
            case LEFT:
                moveLeft();
                drawScene();
                break;
            case RIGHT:
                moveRight();
                drawScene();
                break;
            case UP:
                moveUp();
                drawScene();
                break;
            case DOWN:
                moveDown();
                drawScene();
                break;
        }
    }
}
