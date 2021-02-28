import javax.swing.*;
import java.util.Random;

public class AITetris extends Tetris implements AI {

    JCheckBox debugButton = new JCheckBox();

    AITetris(int pixels) {
        super(pixels);
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

    @Override
    public void startGame() {
        super.startGame();
        random = new Random();  // 用于让形状随机出现
        if (debugButton.isSelected()) {
            random.setSeed(0);
        }
    }
}
