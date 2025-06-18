import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Tetris {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setLayout(null);

            SidePanel sidePanel = new SidePanel();
            Board board = new Board(sidePanel);
            sidePanel.setBoard(board);

            frame.add(sidePanel);
            frame.add(board);

            sidePanel.setBounds(0, 0, 800, 800);
            board.setBounds(250, 100, 300, 600);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}
