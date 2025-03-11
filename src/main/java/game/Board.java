package game;

import igame.IBoard;

import java.util.Random;

public class Board implements IBoard {
    private final int[][] grid;
    private static final int SIZE = 10;
    private final Random random;

    public Board() {
        grid = new int[SIZE][SIZE];
        random = new Random();
    }
    public boolean isCellAttacked(int x, int y) {
        return grid[x][y] == 2 || grid[x][y] == 3; // Клетка уже атакована
    }


    public boolean placeShip(Ship ship, int x, int y, boolean isVertical) {
        int size = ship.getSize();
        if (isVertical) {
            if (y + size > SIZE) return false;
            for (int i = y - 1; i < y + size + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if (i >= 0 && i < SIZE && j >= 0 && j < SIZE && grid[j][i] != 0) {
                        return false; // Проверка на пересечение и соседство
                    }
                }
            }
            for (int i = y; i < y + size; i++) {
                grid[x][i] = 1; // Корабль
            }
        } else {
            if (x + size > SIZE) return false;
            for (int i = x - 1; i < x + size + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 0 && i < SIZE && j >= 0 && j < SIZE && grid[i][j] != 0) {
                        return false; // Проверка на пересечение и соседство
                    }
                }
            }
            for (int i = x; i < x + size; i++) {
                grid[i][y] = 1; // Корабль
            }
        }
        return true;
    }

    public void placeShipsRandomly() {
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Размеры кораблей
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(SIZE);
                int y = random.nextInt(SIZE);
                boolean isVertical = random.nextBoolean();
                Ship ship = new Ship(size);
                placed = placeShip(ship, x, y, isVertical);
            }
        }
    }

    public boolean receiveAttack(int x, int y) {
        if (isCellAttacked(x, y)) {
            return false; // Клетка уже атакована
        }
        if (grid[x][y] == 1) { // Попадание по кораблю
            grid[x][y] = 2; // Помечаем как попадание

            return true;
        } else if (grid[x][y] == 0) { // Промах
            grid[x][y] = 3; // Помечаем как промах

            return false;
        }
        return false; // Если клетка уже была атакована
    }

    public boolean areAllShipsSunk() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 1) { // Если на поле остались непотопленные корабли
                    return false;
                }
            }
        }
        return true; // Все корабли потоплены
    }

    public int[][] getGrid() {
        return grid;
    }
}