package game;

import igame.IGameController;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameController implements IGameController {
    private Board playerBoard;
    private Board computerBoard;
    private Player player;
    private Computer computer;
    private GameView gameView;
    private boolean isPlayerTurn = true; // Флаг для определения хода игрока
    private final int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Размеры кораблей
    private int currentShipIndex = 0; // Индекс текущего корабля для размещения


    public void startGame(Stage primaryStage) {
        playerBoard = new Board();
        computerBoard = new Board();

        // Автоматическая расстановка кораблей компьютера
        computerBoard.placeShipsRandomly();

        player = new Player(playerBoard, computerBoard);
        computer = new Computer(computerBoard, playerBoard);
        gameView = new GameView(primaryStage, player, computer, this);
        gameView.initialize();
        gameView.startShipPlacement(); // Начинаем расстановку кораблей игрока
    }

    /**
     * Возвращает размер текущего корабля для размещения.
     *
     * @return Размер текущего корабля.
     */
    public int getCurrentShipSize() {
        if (currentShipIndex < shipSizes.length) {
            return shipSizes[currentShipIndex];
        }
        return -1; // Все корабли размещены
    }

    /**
     * Размещает корабль игрока на поле.
     *
     * @param x         Координата X.
     * @param y         Координата Y.
     * @param isVertical Ориентация корабля (true — вертикально, false — горизонтально).
     * @return true, если корабль успешно размещен, иначе false.
     */
    public boolean placePlayerShip(int x, int y, boolean isVertical) {
        if (currentShipIndex < shipSizes.length) {
            Ship ship = new Ship(shipSizes[currentShipIndex]);
            if (playerBoard.placeShip(ship, x, y, isVertical)) {
                currentShipIndex++;
                gameView.updateGrid();
                if (currentShipIndex == shipSizes.length) {

                    gameView.startGame();
                }
                return true; // Корабль успешно размещен
            } else {
                return false; // Корабль не удалось разместить
            }
        }
        return false; // Все корабли уже размещены
    }

    public void handlePlayerMove(int x, int y) {
        // Проверяем, что все корабли размещены
        if (currentShipIndex < shipSizes.length) {
            showError("Сначала разместите все корабли!");
            return;
        }

        if (isPlayerTurn) {
            if (computerBoard.isCellAttacked(x, y)) {
                showError("Вы уже стреляли в эту клетку!");
                return;
            }

            boolean isHit = player.makeMove(x, y);
            gameView.updateGrid();

            if (computerBoard.areAllShipsSunk()) {
                gameView.showMessage("Победа!", "Вы потопили все корабли противника!");
                return; // Завершаем игру
            }

            if (!isHit) {
                isPlayerTurn = false; // Переход хода к компьютеру
                computer.makeMoveUntilMiss(); // Ход компьютера до промаха
                gameView.updateGrid();


                    // Проверяем, все ли корабли игрока потоплены
                if (playerBoard.areAllShipsSunk()) {
                        gameView.showMessage("Поражение", "Все ваши корабли потоплены!");
                        return; // Завершаем игру
                }

                isPlayerTurn = true; // Возврат хода к игроку

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



}