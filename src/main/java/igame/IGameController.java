package igame;

import javafx.stage.Stage;

public interface IGameController {
    /**
     * Запускает игру в указанном режиме (PvE или PvP).
     *
     * @param primaryStage Главное окно приложения.
     * @param pvpMode      True, если выбран PvP-режим, false для PvE.
     */
    void startGame(Stage primaryStage, boolean pvpMode);

    /**
     * Размещает корабль игрока на поле.
     *
     * @param x         Координата X.
     * @param y         Координата Y.
     * @param isVertical Ориентация корабля (true — вертикально, false — горизонтально).
     * @return True, если корабль успешно размещен, иначе false.
     */
    boolean placePlayerShip(int x, int y, boolean isVertical);

    /**
     * Обрабатывает ход игрока (атака по координатам).
     *
     * @param x Координата X.
     * @param y Координата Y.
     */
    void handlePlayerMove(int x, int y);

    /**
     * Возвращает размер текущего корабля для размещения.
     *
     * @return Размер текущего корабля или -1, если все корабли размещены.
     */
    int getCurrentShipSize();

    /**
     * Проверяет, является ли текущий режим игры PvP.
     *
     * @return True, если режим PvP, иначе false.
     */
    boolean isPvPMode();

    /**
     * Проверяет, находится ли игра в стадии расстановки кораблей первого игрока (для PvP).
     *
     * @return True, если первый игрок еще расставляет корабли, иначе false.
     */
    boolean isFirstPlayerPlacing();
}