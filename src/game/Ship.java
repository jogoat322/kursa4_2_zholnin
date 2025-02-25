package game;


import igame.IShip;

public class Ship implements IShip {
    private int size;
    private boolean[] hits;
    private int row;
    private int col;
    private boolean isHorizontal;

    public Ship(int size, int row, int col, boolean isHorizontal) {
        this.size = size;
        this.row = row;
        this.col = col;
        this.isHorizontal = isHorizontal;
        this.hits = new boolean[size];
    }

    public int getSize() {
        return size;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public boolean isSunk() {
        for (boolean hit : hits) {
            if (!hit) {
                return false;
            }
        }
        return true;
    }

    public boolean hit(int row, int col) {
        if (isHorizontal) {
            if (row == this.row && col >= this.col && col < this.col + size) {
                hits[col - this.col] = true;
                return true;
            }
        } else {
            if (col == this.col && row >= this.row && row < this.row + size) {
                hits[row - this.row] = true;
                return true;
            }
        }
        return false;
    }
}