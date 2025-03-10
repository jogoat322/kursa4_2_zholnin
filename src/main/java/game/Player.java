package game;

import igame.IPlayer;

public class Player implements IPlayer {
    private Board myBoard;
    private Board opponentBoard;

    public Player(Board myBoard, Board opponentBoard) {
        this.myBoard = myBoard;
        this.opponentBoard = opponentBoard;
    }

    public Board getBoard() {
        return myBoard;
    }

    public boolean makeMove(int x, int y) {
        return opponentBoard.receiveAttack(x, y);
    }
}