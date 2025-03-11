package game;

import igame.IPlayer;

public class Player implements IPlayer {
    private final Board myBoard;
    private final Board opponentBoard;

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