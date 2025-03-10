package igame;

import game.Ship;

public interface IShip {
    int getSize();
    boolean isSunk();
    void hit();
}
