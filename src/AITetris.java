import javax.swing.*;
import java.util.Random;

public class AITetris extends Tetris implements AI {

    protected JCheckBox debugButton;

    AITetris(int pixels) {
        super(pixels);
    }

    public static void main(String[] args) {
        // boilerplate code
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Tetris tetris = new AITetris(16);
        JFrame frame = createFrame(tetris);
        frame.setVisible(true);
    }

    @Override
    public void startGame() {
        random = new Random();  // 用于让形状随机出现
        if (debugButton.isSelected()) {
            random.setSeed(0);
        }
        super.startGame();
    }

    @Override
    public JPanel createControlPanel() {
        JPanel panel = super.createControlPanel();
        debugButton = new JCheckBox("Debug", false);
        panel.add(debugButton);
        return panel;
    }

    @Override
    public void tick(int direction) {
        super.tick(direction);
        if (direction == DOWN) {
            gamingArea.undo();
            Move move = calculateBestMove(gamingArea, currentShape);
            if (move != null) {
                if (move.x > newX) {
                    super.tick(RIGHT);
                } else if (move.x < newX) {
                    super.tick(LEFT);
                } else {
                    super.tick(direction);
                }
                currentShape = move.shape;
                score = (int) move.score;
            }
        }
    }
}
