package igame;

public interface IShip {
    int getSize();
    int getRow();
    int getCol();
    boolean isHorizontal();
    boolean isSunk();
    boolean hit(int row, int col);
}