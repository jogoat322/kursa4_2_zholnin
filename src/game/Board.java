package game;

import igame.IBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board implements IBoard {
    public static final int SIZE = 10; // Размер доски 10x10

    private final char[][] grid;
    private final String name;
    private List<Ship> ships;
    private JButton[][] buttons;

    public Board(String name) {
        this.name = name;
        this.grid = new char[SIZE][SIZE];
        this.ships = new ArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = '~'; // Вода
            }
        }
    }

    @Override
    public void placeShip(int row, int col, int size, boolean isHorizontal) {
        if (!canPlaceShip(row, col, size, isHorizontal)) {
            throw new IllegalArgumentException("Невозможно разместить корабль здесь!");
        }

        Ship ship = new Ship(size, row, col, isHorizontal);
        ships.add(ship);

        if (isHorizontal) {
            for (int i = 0; i < size; i++) {
                grid[row][col + i] = 'S';
            }
        } else {
            for (int i = 0; i < size; i++) {
                grid[row + i][col] = 'S';
            }
        }
    }


    public void placeShipsRandomly() {
        placeRandomShip(4); // 1 корабль на 4 клетки
        placeRandomShip(3); // 2 корабля на 3 клетки
        placeRandomShip(3);
        placeRandomShip(2); // 3 корабля на 2 клетки
        placeRandomShip(2);
        placeRandomShip(2);
        placeRandomShip(1); // 4 корабля на 1 клетку
        placeRandomShip(1);
        placeRandomShip(1);
        placeRandomShip(1);
    }

    private void placeRandomShip(int size) {
        boolean placed = false;
        while (!placed) {
            int row = (int) (Math.random() * SIZE);
            int col = (int) (Math.random() * SIZE);
            boolean isHorizontal = Math.random() < 0.5;

            if (canPlaceShip(row, col, size, isHorizontal)) {
                placeShip(row, col, size, isHorizontal);
                placed = true;
            }
        }
    }

    @Override
    public boolean canPlaceShip(int row, int col, int size, boolean isHorizontal) {
        if (isHorizontal) {
            if (col + size > SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!isCellAvailable(row, col + i)) {
                    return false;
                }
            }
        } else {
            if (row + size > SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!isCellAvailable(row + i, col)) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isCellAvailable(int row, int col) {
        // Проверка, что ячейка и соседние ячейки свободны
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < SIZE && j >= 0 && j < SIZE &&
                        grid[i][j] != '~') {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean shoot(int row, int col) {
        if (grid[row][col] == 'S') {
            for (Ship ship : ships) {
                if (ship.hit(row, col)) {
                    grid[row][col] = 'X'; // Попадание
                    if (ship.isSunk()) {
                        markSunkShip(ship);
                    }
                    return true;
                }
            }
        } else if (grid[row][col] == '~') {
            grid[row][col] = 'O'; // Промах
            return false;
        }
        return false; // Ячейка уже использовалась
    }

    private void markSunkShip(Ship ship) {
        if (ship.isHorizontal()) {
            for (int i = 0; i < ship.getSize(); i++) {
                grid[ship.getRow()][ship.getCol() + i] = 'X';
            }
        } else {
            for (int i = 0; i < ship.getSize(); i++) {
                grid[ship.getRow() + i][ship.getCol()] = 'X';
            }
        }
    }

    @Override
    public boolean isValidShot(int row, int col) {
        return grid[row][col] == '~' || grid[row][col] == 'S';
    }

    @Override
    public boolean isLost() {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public JButton[][] getButtons() {
        return buttons;
    }

    @Override
    public void setButtons(JButton[][] buttons) {
        this.buttons = buttons;
    }

    @Override
    public char[][] getGrid() {
        return grid;
    }
    public void resetBoard() {
        // Очищаем сетку
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = '~'; // Вода
            }
        }

        // Удаляем все корабли
        ships.clear();

        // Очищаем кнопки (если они уже есть)
        if (buttons != null) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    buttons[i][j].setText(""); // Убираем символы "X" и "O"
                    buttons[i][j].setBackground(Color.CYAN); // Возвращаем стандартный цвет
                    buttons[i][j].setEnabled(true); // Делаем кнопку снова активной
                }
            }
        }
    }

}