package igame;

public interface IGame {
    void start();
    void enableShooting();
    void computerMove();
    void updateBoardButtons(IBoard board);
    void updateOrientationLabel();
    void previewShipPlacement(int row, int col);
    void clearPreview();
}
