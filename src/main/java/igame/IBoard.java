package igame;

import game.*;

public interface IBoard {
    boolean placeShip(Ship ship, int x, int y, boolean isVertical);
    boolean receiveAttack(int x, int y);
    boolean isCellAttacked(int x, int y);
    boolean areAllShipsSunk();
    int[][] getGrid();
}
