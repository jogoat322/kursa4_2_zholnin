package game;

import igame.IGameView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;


public class GameView implements IGameView {
    private Stage primaryStage;
    private Player player;
    private Computer computer;
    private GameController gameController;
    private GridPane playerGrid;
    private GridPane computerGrid;
    private Label playerLabel;
    private Label computerLabel;
    private boolean isPlacingShips = false; // Флаг для режима расстановки кораблей
    private int currentShipSize = -1; // Размер текущего корабля для размещения
    private boolean isVertical = false; // Ориентация корабля

    public GameView(Stage primaryStage, Player player, Computer computer, GameController gameController) {
        this.primaryStage = primaryStage;
        this.player = player;
        this.computer = computer;
        this.gameController = gameController;
        playerGrid = new GridPane();
        computerGrid = new GridPane();
        playerLabel = new Label("Ваше поле:");
        computerLabel = new Label("Поле компьютера:");
    }

    public void initialize() {
        // Настройка сетки для поля игрока
        setupGrid(playerGrid, player.getBoard().getGrid(), false);
        // Настройка сетки для поля компьютера (скрытое поле)
        setupGrid(computerGrid, new int[10][10], true);

        // Создание контейнера для полей
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));

        VBox playerBox = new VBox(10, playerLabel, addCoordinates(playerGrid, true));
        VBox computerBox = new VBox(10, computerLabel, addCoordinates(computerGrid, false));
        hbox.getChildren().addAll(playerBox, computerBox);

        // Создание сцены
        Scene scene = new Scene(hbox, 1000, 500); // Увеличим размер окна для лучшего отображения
        primaryStage.setScene(scene);
        primaryStage.setTitle("Морской бой");
        primaryStage.show();

        // Обработка нажатия клавиши Z для изменения ориентации корабля
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.Z && isPlacingShips) {
                isVertical = !isVertical; // Меняем ориентацию
                showMessage("Ориентация корабля", isVertical ? "Вертикально" : "Горизонтально");
                updateShipPreview(); // Обновляем предварительный просмотр
            }
        });
    }

    /**
     * Начинает процесс расстановки кораблей игроком.
     */
    public void startShipPlacement() {
        isPlacingShips = true;
        currentShipSize = gameController.getCurrentShipSize();
        showMessage("Расстановка кораблей", "Разместите ваши корабли на поле. Нажмите Z, чтобы изменить ориентацию.");
        setupGridForShipPlacement(playerGrid);
    }

    /**
     * Завершает расстановку кораблей и начинает игру.
     */
    /**
     * Завершает расстановку кораблей и начинает игру.
     */
    public void startGame() {
        // Проверяем, что все корабли размещены
        if (gameController.getCurrentShipSize() != -1) {
            showError("Ошибка", "Не все корабли размещены!");
            return;
        }
        isPlacingShips = false;
        setupGrid(computerGrid, new int[10][10], true); // Включаем клики по полю компьютера
        showMessage("Игра началась", "Ваш ход!");
    }

    /**
     * Добавляет координаты (буквы и цифры) к игровому полю.
     */
    private GridPane addCoordinates(GridPane grid, boolean isPlayer) {
        GridPane wrapper = new GridPane();
        wrapper.setHgap(5);
        wrapper.setVgap(5);
        wrapper.setPadding(new Insets(10)); // Отступы вокруг поля

        // Добавляем буквы сверху (A-J)
        for (char c = 'A'; c <= 'J'; c++) {
            Label label = new Label(String.valueOf(c));
            label.setMinSize(30, 30);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-weight: bold;"); // Делаем буквы жирными
            wrapper.add(label, c - 'A' + 1, 0);
        }

        // Добавляем цифры слева (1-10)
        for (int i = 1; i <= 10; i++) {
            Label label = new Label(String.valueOf(i));
            label.setMinSize(30, 30);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-weight: bold;"); // Делаем цифры жирными
            wrapper.add(label, 0, i);
        }

        // Добавляем игровое поле
        wrapper.add(grid, 1, 1, 10, 10);

        return wrapper;
    }

    /**
     * Настраивает сетку для игрового поля.
     */
    /**
     * Настраивает сетку для игрового поля.
     */
    private void setupGrid(GridPane grid, int[][] board, boolean isClickable) {
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(5)); // Отступы внутри сетки

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = new Button();
                cell.setMinSize(30, 30);
                cell.setMaxSize(30, 30);


                // Обработка кликов только для поля компьютера
                if (isClickable) {
                    final int x = i;
                    final int y = j;
                    cell.setOnAction(e -> {
                        gameController.handlePlayerMove(x, y);
                        updateCellStyle(cell, computer.getBoard().getGrid()[x][y]); // Принудительно перекрашиваем
                    });
                } else {
                    // Отображение кораблей игрока
                    if (board[i][j] == 1) {
                        cell.setStyle("-fx-background-color: black; -fx-font-size: 12;"); // Корабль
                    }
                }

                grid.add(cell, j, i); // Обратите внимание на порядок индексов (j, i)
            }
        }
    }

    /**
     * Настраивает сетку для расстановки кораблей игроком.
     */
    private void setupGridForShipPlacement(GridPane grid) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = (Button) grid.getChildren().get(i * 10 + j);
                final int x = i;
                final int y = j;

                // Обработка движения мыши для предварительного просмотра
                cell.setOnMouseMoved(e -> {
                    if (isPlacingShips) {
                        showShipPreview(grid, x, y, currentShipSize, isVertical);
                    }
                });

                // Обработка клика для размещения корабля
                cell.setOnMouseClicked(e -> {
                    if (isPlacingShips) {
                        if (gameController.placePlayerShip(x, y, isVertical)) {
                            currentShipSize = gameController.getCurrentShipSize();
                            if (currentShipSize == -1) {
                                // Все корабли размещены, начинаем игру
                                showMessage("Корабли размещены", "Игра начинается!");
                                startGame();
                            }
                        } else {
                            showError("Ошибка", "Невозможно разместить корабль здесь!");
                        }
                    }
                });
            }
        }
    }

    /**
     * Отображает предварительный просмотр корабля на поле.
     */
    private void showShipPreview(GridPane grid, int x, int y, int shipSize, boolean isVertical) {
        // Очищаем предыдущий предварительный просмотр
        clearPreview(grid);

        // Отображаем новый предварительный просмотр
        for (int i = 0; i < shipSize; i++) {
            int previewX = x + (isVertical ? 0 : i);
            int previewY = y + (isVertical ? i : 0);

            if (previewX < 10 && previewY < 10) {
                Button cell = (Button) grid.getChildren().get(previewX * 10 + previewY);
                int cellState = player.getBoard().getGrid()[previewX][previewY];

                // Если клетка не занята кораблём, показываем предварительный просмотр
                if (cellState == 0) { // 0 означает пустую клетку
                    cell.setStyle("-fx-background-color: lightgray; -fx-font-size: 12;"); // Цвет предварительного просмотра
                }
            }
        }
    }


    /**
     * Обновляет предварительный просмотр корабля.
     */
    private void updateShipPreview() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = (Button) playerGrid.getChildren().get(i * 10 + j);
                if (player.getBoard().getGrid()[i][j] == 0) {
                    cell.setStyle("-fx-background-color: white; -fx-font-size: 12;"); // Возвращаем белый цвет
                }
            }
        }
    }

    /**
     * Очищает предварительный просмотр корабля на поле.
     */
    /**
     * Очищает предварительный просмотр корабля на поле.
     */
    private void clearPreview(GridPane grid) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = (Button) grid.getChildren().get(i * 10 + j);
                int cellState = player.getBoard().getGrid()[i][j];

                // Если клетка пустая, возвращаем её в исходное состояние
                if (cellState == 0) {
                    cell.setStyle("-fx-background-color: white; -fx-font-size: 12;"); // Возвращаем белый цвет
                }

            }
        }
    }

    /**
     * Обновляет отображение игровых полей.
     */
    public void updateGrid() {
        // Обновление поля игрока
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = (Button) playerGrid.getChildren().get(i * 10 + j);
                int cellState = player.getBoard().getGrid()[i][j];
                updateCellStyle(cell, cellState);
            }
        }

        // Обновление поля компьютера
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button cell = (Button) computerGrid.getChildren().get(i * 10 + j);
                int cellState = computer.getBoard().getGrid()[i][j];

                // Обновляем стиль клетки, если она была атакована (попадание или промах)
                if (cellState == 2 || cellState == 3) {
                    updateCellStyle(cell, cellState);
                }
            }
        }
    }

    /**
     * Обновляет стиль клетки в зависимости от её состояния.
     */
    /**
     * Обновляет стиль клетки в зависимости от её состояния.
     */
    private void updateCellStyle(Button cell, int cellState) {
        switch (cellState) {
            case 0: // Пустая клетка
                cell.setStyle("-fx-background-color: white; -fx-font-size: 12;");
                break;
            case 1: // Корабль (не показываем на поле компьютера)
                cell.setStyle("-fx-background-color: black; -fx-font-size: 12;");
                break;
            case 2: // Попадание
                cell.setStyle("-fx-background-color: red; -fx-font-size: 12;"); // Оставляем белый фо
                break;
            case 3: // Промах
                cell.setStyle("-fx-background-color: blue; -fx-font-size: 12;");
                break;
        }
    }


    /**
     * Показывает информационное сообщение.
     */
    public void showMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Показывает сообщение об ошибке.
     */
    public void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(e -> {
            if (title.equals("Победа!") || title.equals("Поражение")) {
                primaryStage.close(); // Закрываем игру
            }
        });
        alert.showAndWait();
    }

}