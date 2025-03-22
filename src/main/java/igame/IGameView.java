package igame;

public interface IGameView {
    void initialize();
    void startShipPlacement();
    void startGame();
    void updateGrid();
    void showMessage(String title, String message);
    void showError(String title, String message);
}