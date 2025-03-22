package game;

import igame.IGameController;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameController implements IGameController {
    private Board player1Board; // Поле первого игрока
    private Board player2Board; // Поле второго игрока (или компьютера в PvE)
    private Player player1;
    private Player player2; // Для PvP
    private Computer computer; // Для PvE
    private GameView gameView;
    private boolean isPlayerTurn = true; // Флаг для определения хода игрока (пока не используется в PvP)
    private final int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Размеры кораблей
    private int currentShipIndex = 0; // Индекс текущего корабля для размещения
    private boolean isPvPMode = false; // Флаг режима PvP
    private boolean isFirstPlayerPlacing = true; // Флаг для определения, кто сейчас расставляет корабли

    // Конструктор по умолчанию
    public GameController() {
    }

    public void startGame(Stage primaryStage, boolean pvpMode) {
        isPvPMode = pvpMode;
        player1Board = new Board();

        if (isPvPMode) {
            player2Board = new Board();
            player1 = new Player(player1Board, player2Board);
            player2 = new Player(player2Board, player1Board);
            gameView = new GameView(primaryStage, player1, player2, this);
        } else {
            player2Board = new Board();
            player2Board.placeShipsRandomly(); // Компьютер расставляет корабли
            player1 = new Player(player1Board, player2Board);
            computer = new Computer(player2Board, player1Board);
            gameView = new GameView(primaryStage, player1, computer, this);
        }

        gameView.initialize();
        gameView.startShipPlacement(); // Начинаем расстановку кораблей
    }

    public int getCurrentShipSize() {
        if (currentShipIndex < shipSizes.length) {
            return shipSizes[currentShipIndex];
        }
        return -1; // Все корабли размещены
    }

    public boolean placePlayerShip(int x, int y, boolean isVertical) {
        if (currentShipIndex < shipSizes.length) {
            Ship ship = new Ship(shipSizes[currentShipIndex]);
            Board currentBoard = isPvPMode && isFirstPlayerPlacing ? player1Board : player1Board;
            if (isPvPMode && !isFirstPlayerPlacing) {
                currentBoard = player2Board;
            }

            if (currentBoard.placeShip(ship, x, y, isVertical)) {
                currentShipIndex++;
                gameView.updateGrid();
                if (currentShipIndex == shipSizes.length) {
                    if (isPvPMode && isFirstPlayerPlacing) {
                        // Первый игрок закончил расстановку, переходим ко второму
                        isFirstPlayerPlacing = false;
                        currentShipIndex = 0; // Сбрасываем для второго игрока
                        gameView.switchToSecondPlayerPlacement();
                    } else if (isPvPMode && !isFirstPlayerPlacing) {
                        // Второй игрок закончил, пока не начинаем игру
                        gameView.showMessage("Расстановка завершена", "Оба игрока разместили корабли.");
                    } else {
                        // PvE-режим, начинаем игру
                        gameView.startGame();
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void handlePlayerMove(int x, int y) {
        if (isPvPMode) {
            showError("PvP-режим пока не поддерживает ходы!");
            return;
        }

        if (currentShipIndex < shipSizes.length) {
            showError("Сначала разместите все корабли!");
            return;
        }

        if (isPlayerTurn) {
            if (player2Board.isCellAttacked(x, y)) {
                showError("Вы уже стреляли в эту клетку!");
                return;
            }

            boolean isHit = player1.makeMove(x, y);
            gameView.updateGrid();

            if (player2Board.areAllShipsSunk()) {
                gameView.showMessage("Победа!", "Вы потопили все корабли противника!");
                return;
            }

            if (!isHit) {
                isPlayerTurn = false;
                computer.makeMoveUntilMiss();
                gameView.updateGrid();

                if (player1Board.areAllShipsSunk()) {
                    gameView.showMessage("Поражение", "Все ваши корабли потоплены!");
                    return;
                }

                isPlayerTurn = true;
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isPvPMode() {
        return isPvPMode;
    }

    public boolean isFirstPlayerPlacing() {
        return isFirstPlayerPlacing;
    }
}