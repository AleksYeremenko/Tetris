import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Random;

public final class Board extends JPanel {
    private final int ROWS = 20;
    private final int COLS = 10;
    private final int CELL_SIZE = 30;

    private final boolean[][] cells = new boolean[ROWS][COLS];

    private Shapes shapes;
    private final SidePanel sidePanel;

    private int m_interval = 500;
    private final Timer m_timer;

    private boolean gameOver = false;
    private final JLabel labelGameOver;

    public Board(SidePanel sideP) {
        this.setBackground(UIManager.getColor("Panel.background"));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setLayout(null);
        this.setBorder(new LineBorder(UIManager.getColor("Panel.foreground"), 3));

        labelGameOver = new JLabel("GAME OVER", SwingConstants.CENTER);
        labelGameOver.setBounds(0, (ROWS * CELL_SIZE) / 2 - 20, COLS * CELL_SIZE, 40);
        labelGameOver.setFont(new FontUIResource("Arial", FontUIResource.BOLD, 20));
        labelGameOver.setForeground(UIManager.getColor("Label.foreground"));
        labelGameOver.setVisible(false);
        this.add(labelGameOver);

        sidePanel = sideP;
        sidePanel.setBoard(Board.this);

        m_timer = new Timer(m_interval, e -> {
            if (!gameOver) {
                if (shapes != null) {
                    shapes.dropElement(Board.this);
                    checkForFullLines();
                }
                repaint();
            }
        });

        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke('a'), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shapes != null && !gameOver) {
                    shapes.moveLeft(Board.this);
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('d'), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shapes != null && !gameOver) {
                    shapes.moveRight(Board.this);
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('s'), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shapes != null && !gameOver) {
                    shapes.dropElement(Board.this);
                    checkForFullLines();
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('w'), "rotate");
        actionMap.put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shapes != null && !gameOver) {
                    shapes.rotate(Board.this);
                    repaint();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('p'), "pause");
        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shapes != null && !gameOver) {
                    sidePanel.pauseAction();
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke('r'), "reset");
        actionMap.put("reset", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sidePanel.startAction();
            }
        });
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

        if (shapes != null && shapes.hasReached()) {
            int[] temp = shapes.getPosition();
            if (temp[1] <= 0) {
                gameOverDisplay();
            }
        }

        if (!gameOver) {
            g.setColor(UIManager.getColor("Button.foreground"));
            for (int i = 0; i <= cells[0].length; i++) {
                int x = i * CELL_SIZE;
                g.drawLine(x, 0, x, ROWS * CELL_SIZE);
            }
            for (int i = 0; i <= cells.length; i++) {
                int y = i * CELL_SIZE;
                g.drawLine(0, y, COLS * CELL_SIZE, y);
            }

            if (shapes != null) {
                fillShapes(g);
                fillBoard(g);
            }

            if (shapes != null && shapes.hasReached()) {
                shapeGenerator();
            }
        }
    }

    public void shapeGenerator() {
        int idx;
        Random rand = new Random();
        String[] shape = {"L", "RL", "S", "Z", "T", "SQ", "I"};
        Integer[] angles = {0, 1, 2, 3};
        idx = rand.nextInt(shape.length);
        String randomShape = shape[idx];
        idx = rand.nextInt(angles.length);
        int randomAngle = angles[idx];

        this.shapes = new Shapes(randomShape, randomAngle);
        sidePanel.incrementShapeCount(randomShape);
    }

    public void boardInitializer() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = false;
            }
        }
    }

    public void drawCell(java.awt.Graphics g, int x, int y) {
        g.setColor(UIManager.getColor("Button.foreground"));
        g.fill3DRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1, true);
        if (shapes.hasReached()) {
            cells[y][x] = true;
        }
    }

    public void fillShapes(java.awt.Graphics g) {
        int[] a = shapes.getPosition();
        int[] x = shapes.getX();
        int[] y = shapes.getY();
        for (int i = 0; i < 4; i++) {
            drawCell(g, a[0] + x[i], a[1] + y[i]);
        }
    }

    public void fillBoard(java.awt.Graphics g) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (cells[i][j]) {
                    drawCell(g, j, i);
                }
            }
        }
    }

    public void gameOverDisplay() {
        gameOver = true;
        labelGameOver.setVisible(true);
        saveGameData();
        repaint();
    }

    public boolean[][] returnGrid() {
        return cells;
    }

    public void startGame(String difficulty) {
        switch (difficulty) {
            case "Easy" -> m_interval = 1000;
            case "Medium" -> m_interval = 500;
            case "Hard" -> m_interval = 200;
        }
        m_timer.setDelay(m_interval);
        gameOver = false;
        labelGameOver.setVisible(false);
        boardInitializer();
        shapeGenerator();
        sidePanel.setScore(0);
        sidePanel.setLines(0);
        m_timer.start();
    }

    public void resetGame() {
        m_timer.stop();
        boardInitializer();
        sidePanel.setScore(0);
        sidePanel.setLines(0);
        shapeGenerator();
        gameOver = false;
        labelGameOver.setVisible(false);
        repaint();
    }

    public void setAnimation(boolean state) {
        if (state) {
            m_timer.start();
        } else {
            m_timer.stop();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void checkForFullLines() {
        int fullLines = 0;
        for (int i = 0; i < ROWS; i++) {
            boolean isFull = true;
            for (int j = 0; j < COLS; j++) {
                if (!cells[i][j]) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                fullLines++;
                removeLine(i);
            }
        }
        if (fullLines > 0) {
            int score = sidePanel.getScore() + (40 * fullLines);
            int lines = sidePanel.getLines() + fullLines;
            sidePanel.setScore(score);
            sidePanel.setLines(lines);
        }
    }

    private void removeLine(int row) {
        for (int i = row; i > 0; i--) {
            System.arraycopy(cells[i - 1], 0, cells[i], 0, COLS);
        }
        for (int j = 0; j < COLS; j++) {
            cells[0][j] = false;
        }
    }

    public void saveGameData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("game_data.txt"))) {
            writer.println("Score: " + sidePanel.getScore());
            writer.println("Lines: " + sidePanel.getLines());
            writer.println("Board:");
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    writer.print(cells[i][j] ? "1" : "0");
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("game_data.txt"))) {
            String line;
            line = reader.readLine();
            int score = Integer.parseInt(line.split(": ")[1]);
            line = reader.readLine();
            int lines = Integer.parseInt(line.split(": ")[1]);
            sidePanel.setScore(score);
            sidePanel.setLines(lines);
            reader.readLine();
            for (int i = 0; i < ROWS; i++) {
                line = reader.readLine();
                for (int j = 0; j < COLS; j++) {
                    cells[i][j] = line.charAt(j) == '1';
                }
            }
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
