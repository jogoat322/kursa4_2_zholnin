package game;

import igame.IBoard;
import igame.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static game.Board.SIZE;

public class Game implements IGame {
    private final Board playerBoard;
    private final Board computerBoard;
    private int currentShipSize;
    private int shipsToPlace;
    private boolean isHorizontal;
    private JFrame frame;
    private JLabel instructionLabel;
    private JLabel orientationLabel;
    private JButton[][] playerButtons;
    private int lastPreviewRow = -1;
    private int lastPreviewCol = -1;
    private boolean firstGame = true;

    public Game() {
        playerBoard = new Board("Player");
        computerBoard = new Board("Computer");

        // Компьютер случайно размещает корабли
        computerBoard.placeShipsRandomly();

        // Начальная настройка кораблей игрока
        currentShipSize = 4;
        shipsToPlace = 1;
        isHorizontal = true;
    }


    @Override
    public void start() {

        if (!showStartMenu()) {
            System.exit(0);
        }
        firstGame = false;

        frame = new JFrame("Морской бой");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Фоновая панель
        BackgroundPanel mainPanel = new BackgroundPanel("C:\\Users\\Andrey\\Desktop\\kursa4_2\\png\\wow.png");
        mainPanel.setLayout(new BorderLayout());

        JPanel playerPanel = createBoardPanel(playerBoard, "Ваш флот", true);
        JPanel computerPanel = createBoardPanel(computerBoard, "Флот противника", false);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        instructionLabel = new JLabel("Разместите корабли: 1 корабль на 4 клетки", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        orientationLabel = new JLabel("Ориентация: Горизонтальная", SwingConstants.CENTER);
        orientationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(instructionLabel);
        infoPanel.add(orientationLabel);
        infoPanel.setOpaque(false); // Делаем панель прозрачной

        mainPanel.add(playerPanel, BorderLayout.WEST);
        mainPanel.add(computerPanel, BorderLayout.EAST);
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        frame.setContentPane(mainPanel);
        frame.setSize(1000, 500);
        frame.setFocusable(true); // Делаем окно активным для ввода
        frame.requestFocusInWindow(); // Запрашиваем фокус клавиатуры
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    isHorizontal = !isHorizontal;
                    updateOrientationLabel();
                    if (lastPreviewRow != -1 && lastPreviewCol != -1) {
                        previewShipPlacement(lastPreviewRow, lastPreviewCol);
                    }
                }
            }
        });

// Добавляем `KeyListener` к `mainPanel`


    }

    private boolean showStartMenu() {
        String[] options = {"Играть против компьютера", "Выйти"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Выберите действие",
                "Морской бой",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        return choice == 0;
    }

    @Override
    public void updateOrientationLabel() {
        String orientation = isHorizontal ? "Горизонтальная" : "Вертикальная";
        orientationLabel.setText("Ориентация: " + orientation);
    }

    private JPanel createBoardPanel(IBoard board, String title, boolean isPlayerBoard) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(SIZE + 1, SIZE + 1));
        gridPanel.add(new JLabel(""));
        for (int i = 0; i < SIZE; i++) {
            gridPanel.add(new JLabel(Character.toString((char) ('А' + i)), SwingConstants.CENTER));
        }

        playerButtons = new JButton[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            gridPanel.add(new JLabel(Integer.toString(i + 1), SwingConstants.CENTER));

            for (int j = 0; j < SIZE; j++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.PLAIN, 16));
                button.setBackground(Color.CYAN);

                if (isPlayerBoard) {
                    final int row = i;
                    final int col = j;
                    button.addActionListener(e -> placeShip(row, col));
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            previewShipPlacement(row, col);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            clearPreview();
                        }
                    });
                }

                playerButtons[i][j] = button;
                gridPanel.add(button);
            }
        }

        board.setButtons(playerButtons);
        panel.add(gridPanel, BorderLayout.CENTER);
        return panel;
    }


    @Override
    public void updateBoardButtons(IBoard board) {
        JButton[][] buttons = board.getButtons();
        char[][] grid = board.getGrid();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 'S') {
                    buttons[i][j].setBackground(Color.BLACK); // Отобразить корабли игрока
                }
            }
        }
    }

    private void placeShip(int row, int col) {
        if (shipsToPlace > 0) {
            if (playerBoard.canPlaceShip(row, col, currentShipSize, isHorizontal)) {
                playerBoard.placeShip(row, col, currentShipSize, isHorizontal);
                updateBoardButtons(playerBoard);
                shipsToPlace--;

                if (shipsToPlace == 0) {
                    switch (currentShipSize) {
                        case 4 -> {
                            currentShipSize = 3;
                            shipsToPlace = 2;
                        }
                        case 3 -> {
                            currentShipSize = 2;
                            shipsToPlace = 3;
                        }
                        case 2 -> {
                            currentShipSize = 1;
                            shipsToPlace = 4;
                        }
                        case 1 -> {
                            instructionLabel.setText("Все корабли размещены! Игра начинается.");
                            startGame();
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Невозможно разместить корабль здесь!");
            }
        }
        frame.requestFocusInWindow();
    }

    @Override
    public void previewShipPlacement(int row, int col) {
        JButton[][] buttons = playerBoard.getButtons();
        char[][] grid = playerBoard.getGrid();

        boolean canPlace = playerBoard.canPlaceShip(row, col, currentShipSize, isHorizontal);

        for (int i = 0; i < currentShipSize; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r < SIZE && c < SIZE) {
                // Если в клетке уже есть корабль, оставляем её черной
                if (grid[r][c] == 'S') {
                    buttons[r][c].setBackground(Color.BLACK);
                } else {
                    // Если можно разместить корабль — зелёный, иначе красный
                    buttons[r][c].setBackground(canPlace ? Color.GREEN : Color.RED);
                }
            }
        }
    }


    @Override
    public void clearPreview() {
        JButton[][] buttons = playerBoard.getButtons();
        char[][] grid = playerBoard.getGrid();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 'S') {
                    buttons[i][j].setBackground(Color.BLACK); // Оставляем корабли черными
                } else if (grid[i][j] == 'X') {
                    buttons[i][j].setBackground(Color.RED); // Оставляем попадания красными
                    buttons[i][j].setText("X");
                } else if (grid[i][j] == 'O') {
                    buttons[i][j].setBackground(Color.GRAY); // Оставляем промахи серыми
                    buttons[i][j].setText("O");
                } else {
                    buttons[i][j].setBackground(Color.CYAN); // Очищаем только свободные клетки
                }
            }
        }
    }


    public void startGame() {
        frame.remove(instructionLabel);
        frame.remove(orientationLabel);
        frame.revalidate();
        frame.repaint();
        enableShooting();
    }

    @Override
    public void enableShooting() {
        JButton[][] computerButtons = computerBoard.getButtons();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                final int row = i;
                final int col = j;
                computerButtons[row][col].addActionListener(e -> {
                    if (computerBoard.isValidShot(row, col)) {
                        if (computerBoard.shoot(row, col)) {
                            computerButtons[row][col].setText("X");
                            computerButtons[row][col].setBackground(Color.RED);
                            if (computerBoard.isLost()) {
                                showEndMenu("Вы победили! 🎉");
                            }
                        } else {
                            computerButtons[row][col].setText("O");
                            computerButtons[row][col].setBackground(Color.GRAY);
                            computerMove();
                        }
                        computerButtons[row][col].setEnabled(false);
                    }
                });
            }
        }
    }

    @Override
    public void computerMove() {
        int row, col;
        do {
            row = (int) (Math.random() * SIZE);
            col = (int) (Math.random() * SIZE);
        } while (!playerBoard.isValidShot(row, col));

        JButton[][] playerButtons = playerBoard.getButtons();

        if (playerBoard.shoot(row, col)) {
            playerButtons[row][col].setText("X");
            playerButtons[row][col].setBackground(Color.RED);
            if (playerBoard.isLost()) {
                showEndMenu("Компьютер победил! 😞");
            } else {
                computerMove();
            }
        } else {
            playerButtons[row][col].setText("O");
            playerButtons[row][col].setBackground(Color.GRAY);
        }
        playerButtons[row][col].setEnabled(false);
    }

    private void showEndMenu(String message) {
        String[] options = {"Сыграть снова", "Выйти"};
        int choice = JOptionPane.showOptionDialog(
                null,
                message,
                "Конец игры",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        playerBoard.resetBoard(); // Сбрасываем доску игрока
        computerBoard.resetBoard(); // Сбрасываем доску компьютера
        computerBoard.placeShipsRandomly(); // Компьютер снова размещает корабли
        enableShooting();
    }
}
