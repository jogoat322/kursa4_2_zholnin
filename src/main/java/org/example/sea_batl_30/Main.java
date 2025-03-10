package org.example.sea_batl_30;

import game.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage);
        mainMenu.show(); // Показываем главное меню
    }

    public static void main(String[] args) {
        launch(args);
    }
}