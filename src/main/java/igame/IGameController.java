package igame;

public interface IGameController {
    void startGame(javafx.stage.Stage primaryStage);
    boolean placePlayerShip(int x, int y, boolean isVertical);
    void handlePlayerMove(int x, int y);
}
