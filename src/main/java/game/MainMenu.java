package game;

import igame.IMainMenu;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class MainMenu implements IMainMenu {
    private final Stage primaryStage;

    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        File file = new File("C:\\Users\\Andrey\\Desktop\\3kurs\\sea_batl_30\\src\\main\\resources\\world-of-warships-1k7yh.jpg");
        Image backgroundImage = new Image(file.toURI().toString());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(primaryStage.getWidth());
        backgroundView.setFitHeight(primaryStage.getHeight());
        backgroundView.setPreserveRatio(false); // Растягиваем на весь экран

        // Контейнер для кнопок
        VBox menuBox = new VBox(40);
        menuBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Кнопка "Играть против компьютера"
        Button playButtonPvE = new Button("Играть против компьютера");
        playButtonPvE.setMinSize(300, 80);
        playButtonPvE.setStyle("-fx-font-size: 24px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        playButtonPvE.setOnAction(e -> startGamePvE());

        // Кнопка "Играть против игрока"
        Button playButtonPvP = new Button("Играть против игрока");
        playButtonPvP.setMinSize(300, 80);
        playButtonPvP.setStyle("-fx-font-size: 24px; -fx-background-color: #FF9800; -fx-text-fill: white;");
        playButtonPvP.setOnAction(e -> showPvPModeMessage());

        // Кнопка "Выйти"
        Button exitButton = new Button("Выйти");
        exitButton.setMinSize(300, 80);
        exitButton.setStyle("-fx-font-size: 24px; -fx-background-color: #f44336; -fx-text-fill: white;");
        exitButton.setOnAction(e -> primaryStage.close());

        menuBox.getChildren().addAll(playButtonPvE, playButtonPvP, exitButton);

        // Используем StackPane, чтобы фон был позади кнопок
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, menuBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Главное меню");
        primaryStage.show();
    }

    private void startGamePvE() {
        GameController gameController = new GameController();
        gameController.startGame(primaryStage); // Запуск игры против компьютера
    }

    private void showPvPModeMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Режим в разработке");
        alert.setHeaderText(null);
        alert.setContentText("Режим 'Игрок против игрока' пока в разработке. Следите за обновлениями!");
        alert.showAndWait();
    }
}
