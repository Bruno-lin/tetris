import javax.swing.*;
import java.util.Random;

public class AITetris extends Tetris implements AI {

     JCheckBox debugButton;

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
        count = 0;
        score = 0;
        gameOn = true;

        if (debugButton.isSelected()) {
            random.setSeed(0);
        }else random = new Random();

        startTime = System.currentTimeMillis();
        timer.start();


        gamingArea = new GamingArea(WIDTH, HEIGHT + TOP_SPACE);
        updateCounters();
        toggleButtons();
        timeLabel.setText(" ");
        addNewShape();

        repaint();
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
                score = (int) move.score;
                currentX = move.x;
                currentY = move.y;
                currentShape = move.shape;
            }
        }
    }
}
