package game;

import java.util.Random;

import igame.IComputer;

public class Computer implements IComputer {
    private Board myBoard;
    private Board opponentBoard;
    private Random random;

    public Computer(Board myBoard, Board opponentBoard) {
        this.myBoard = myBoard;
        this.opponentBoard = opponentBoard;
        this.random = new Random();
    }

    public Board getBoard() {
        return myBoard;
    }

    public void makeMoveUntilMiss() {
        boolean isHit;
        do {
            int x, y;

            // Генерируем новые координаты, пока не найдём свободную клетку
            do {
                x = random.nextInt(10);
                y = random.nextInt(10);
            } while (opponentBoard.isCellAttacked(x, y)); // Проверяем, не стреляли ли сюда раньше

            isHit = opponentBoard.receiveAttack(x, y);
        } while (isHit); // Если попал, стреляет дальше
    }

}