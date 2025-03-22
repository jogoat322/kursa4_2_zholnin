package game;

import igame.IGameController;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameController implements IGameController {
    private Board player1Board;
    private Board player2Board;
    private Player player1;
    private Player player2;
    private Computer computer;
    private GameView gameView;
    private boolean isPlayerTurn = true;
    private final int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    private int currentShipIndex = 0;
    private boolean isPvPMode = false;
    private boolean isFirstPlayerPlacing = true;

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
            player2Board.placeShipsRandomly();
            player1 = new Player(player1Board, player2Board);
            computer = new Computer(player2Board, player1Board);
            gameView = new GameView(primaryStage, player1, computer, this);
        }

        gameView.initialize();
        gameView.startShipPlacement();
    }

    public int getCurrentShipSize() {
        if (currentShipIndex < shipSizes.length) {
            return shipSizes[currentShipIndex];
        }
        return -1;
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
                        isFirstPlayerPlacing = false;
                        currentShipIndex = 0;
                        gameView.hidePlayer1Grid(); // "Скрываем" поле первого игрока
                        gameView.switchToSecondPlayerPlacement();
                    } else if (isPvPMode && !isFirstPlayerPlacing) {
                        gameView.hidePlayer2Grid(); // "Скрываем" оба поля после второго игрока
                        gameView.showMessage("Расстановка завершена", "Оба поля готовы для игры.");
                    } else {
                        gameView.startGame(); // PvE-режим
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