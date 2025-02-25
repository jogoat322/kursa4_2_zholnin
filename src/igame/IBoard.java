package igame;

import javax.swing.*;



public interface IBoard {
    void placeShip(int row, int col, int size, boolean isHorizontal);
    boolean canPlaceShip(int row, int col, int size, boolean isHorizontal);
    boolean shoot(int row, int col);
    boolean isValidShot(int row, int col);
    boolean isLost();
    JButton[][] getButtons();
    void setButtons(JButton[][] buttons);
    char[][] getGrid();
    void resetBoard();
}