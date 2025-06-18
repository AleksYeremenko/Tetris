import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import java.util.Objects;

public class SidePanel extends JPanel {

    private boolean paused = false;
    private boolean started = false;

    private final JLabel labelScoreUpdate;
    private final JLabel labelLinesUpdate;
    private final JButton startButton;
    private final JButton pauseButton;
    private final JComboBox<String> difficultyComboBox;
    private final JLabel[] shapeStats;
    private final int[] shapeCounts = new int[7];

    private Board board;

    private int score = 0;
    private int lines = 0;

    public SidePanel() {
        this.setLayout(null);
        this.setBackground(UIManager.getColor("Panel.background"));
        this.setBorder(new LineBorder(UIManager.getColor("Panel.foreground"), 3));

        JLabel labelScore = new JLabel();
        labelScoreUpdate = new JLabel();
        JLabel labelLines = new JLabel();
        labelLinesUpdate = new JLabel();
        startButton = new JButton();
        pauseButton = new JButton();
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");
        difficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        shapeStats = new JLabel[7];

        setupLabel(labelScore, "Score", 10, 24);
        setupLabel(labelScoreUpdate, String.format("%04d", score), 40, 38);
        setupLabel(labelLines, "Lines", 90, 24);
        setupLabel(labelLinesUpdate, String.format("%04d", lines), 120, 38);

        setupButton(startButton, "Start", 10);
        startButton.addActionListener(evt -> startAction());

        setupButton(pauseButton, "Pause", 40);
        pauseButton.addActionListener(evt -> pauseAction());

        setupButton(saveButton, "Save Game", 70);
        saveButton.addActionListener(evt -> saveGame());

        setupButton(loadButton, "Load Game", 100);
        loadButton.addActionListener(evt -> loadGame());

        difficultyComboBox.setBounds(650, 130, 100, 30);
        difficultyComboBox.setBackground(UIManager.getColor("Button.background"));
        difficultyComboBox.setForeground(UIManager.getColor("Button.foreground"));
        this.add(difficultyComboBox);

        for (int i = 0; i < shapeStats.length; i++) {
            shapeStats[i] = new JLabel();
            shapeStats[i].setForeground(UIManager.getColor("Label.foreground"));
            shapeStats[i].setBounds(10, 300 + i * 30, 100, 24);
            this.add(shapeStats[i]);
        }
        updateShapeStats();
    }

    private void setupLabel(JLabel label, String text, int y, int height) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(UIManager.getColor("Label.foreground"));
        label.setFont(new FontUIResource("Arial", FontUIResource.BOLD, 16));
        label.setText(text);
        label.setOpaque(true);
        label.setBackground(UIManager.getColor("Label.background"));
        label.setBounds(10, y, 100, height);
        this.add(label);
    }

    private void setupButton(JButton button, String text, int y) {
        button.setText(text);
        button.setBackground(UIManager.getColor("Button.background"));
        button.setForeground(UIManager.getColor("Button.foreground"));
        button.setBounds(650, y, 100, 30);
        this.add(button);
    }

    public void setScore(int s) {
        score = s;
        labelScoreUpdate.setText(String.format("%04d", s));
    }

    public void setLines(int l) {
        lines = l;
        labelLinesUpdate.setText(String.format("%04d", l));
    }

    public void setBoard(Board b) {
        board = b;
    }

    private void setStart(boolean state) {
        started = state;
    }

    private boolean isStarted() {
        return started;
    }

    void startAction() {
        if (!isStarted()) {
            String selectedDifficulty = (String) Objects.requireNonNull(difficultyComboBox.getSelectedItem());
            board.startGame(selectedDifficulty);
            startButton.setText("Reset");
            setStart(true);
        } else {
            board.resetGame();
            startButton.setText("Start");
            setStart(false);
        }
    }

    private void setPaused(boolean state) {
        paused = state;
    }

    private boolean isPaused() {
        return paused;
    }

    void pauseAction() {
        if (started && !board.isGameOver()) {
            if (!isPaused()) {
                board.setAnimation(false);
                pauseButton.setText("Resume");
                setPaused(true);
            } else {
                board.setAnimation(true);
                pauseButton.setText("Pause");
                setPaused(false);
            }
        }
    }

    private void saveGame() {
        if (started) {
            board.saveGameData();
        }
    }

    private void loadGame() {
        if (!started) {
            board.loadGameData();
            startButton.setText("Reset");
            setStart(true);
        }
    }

    public int getScore() {
        return score;
    }

    public int getLines() {
        return lines;
    }

    public void incrementShapeCount(String shape) {
        switch (shape) {
            case "L" -> shapeCounts[0]++;
            case "RL" -> shapeCounts[1]++;
            case "S" -> shapeCounts[2]++;
            case "Z" -> shapeCounts[3]++;
            case "T" -> shapeCounts[4]++;
            case "SQ" -> shapeCounts[5]++;
            case "I" -> shapeCounts[6]++;
        }
        updateShapeStats();
    }

    private void updateShapeStats() {
        String[] shapeNames = {"L", "RL", "S", "Z", "T", "SQ", "I"};
        for (int i = 0; i < shapeStats.length; i++) {
            shapeStats[i].setText(shapeNames[i] + ": " + shapeCounts[i]);
        }
    }
}
