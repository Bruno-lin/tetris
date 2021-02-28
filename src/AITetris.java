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
        random = new Random();  // 用于让形状随机出现
        if (debugButton.isSelected()) {
            random.setSeed(0);
        }
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

    @Override
    public Shape pickNextShape() {
        super.pickNextShape();
        Shape AI_pick = shapes[0];
        int best_score = 0;
        for (Shape shape : shapes) {
            gamingArea.undo();
            Move move = calculateBestMove(gamingArea, shape);
            if(move == null){
                return shape;
            }else if (move.score > best_score){
                best_score = (int)move.score;
                AI_pick = shape;
            }
        }
        return AI_pick;
    }
}
