package game;

import igame.IMainMenu;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        File file = new File("C:\\Users\\Andrey\\Desktop\\3kurs\\sea_batl_30\\src\\main\\java\\png\\world-of-warships-1k7yh.jpg");
        Image backgroundImage = new Image(file.toURI().toString());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(primaryStage.getWidth());
        backgroundView.setFitHeight(primaryStage.getHeight());
        backgroundView.setPreserveRatio(false); // Растягиваем на весь экран

        // Контейнер для кнопок
        VBox menuBox = new VBox(40);
        menuBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Кнопка "Играть против компьютера"
        Button playButton = new Button("Играть против компьютера");
        playButton.setMinSize(300, 80);
        playButton.setStyle("-fx-font-size: 24px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        playButton.setOnAction(e -> startGame());

        // Кнопка "Выйти"
        Button exitButton = new Button("Выйти");
        exitButton.setMinSize(300, 80);
        exitButton.setStyle("-fx-font-size: 24px; -fx-background-color: #f44336; -fx-text-fill: white;");
        exitButton.setOnAction(e -> primaryStage.close());

        menuBox.getChildren().addAll(playButton, exitButton);

        // Используем StackPane, чтобы фон был позади кнопок
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, menuBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("Главное меню");
        primaryStage.show();
    }

    private void startGame() {
        GameController gameController = new GameController();
        gameController.startGame(primaryStage);
    }
}
