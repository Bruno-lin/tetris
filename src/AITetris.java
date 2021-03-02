import javax.swing.*;
import java.util.Random;
import java.util.function.BiFunction;

public class AITetris extends Tetris implements AI {

    private JCheckBox debugButton;
    private JCheckBox AIButton;

    private BiFunction<Shape[], Random, Shape> pickShapeFunction;

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
        random = new Random();
        if (debugButton.isSelected()) {
            random.setSeed(0);
        }
        super.startGame();
    }

    /**
     * 添加按钮
     * @return
     */
    @Override
    public JPanel createControlPanel() {
        JPanel panel = super.createControlPanel();
        panel.add(Box.createVerticalStrut(20));

        ButtonGroup difficultyGroup = new ButtonGroup();
        JRadioButton normalButton = new JRadioButton("Normal");
        JRadioButton difficultButton = new JRadioButton("Difficult");
        addButton(difficultyGroup, panel, normalButton);
        addButton(difficultyGroup, panel, difficultButton);
        panel.add(Box.createVerticalStrut(20));

        normalButton.addActionListener(e ->
                pickShapeFunction = this::pickNormalShape);

        difficultButton.addActionListener(e ->
                pickShapeFunction = this::pickDifficultShape);

        AIButton = new JCheckBox("AI", false);
        panel.add(AIButton);

        debugButton = new JCheckBox("Debug", false);
        panel.add(debugButton);

        return panel;
    }

    private void addButton(ButtonGroup buttonGroup, JPanel buttonPanel, JRadioButton button) {
        button.setFocusable(false);
        buttonGroup.add(button);
        buttonPanel.add(button);
    }

    @Override
    public void tick(int direction) {
        super.tick(direction);
        if (AIButton.isSelected()) {
            gamingArea.undo();
            Move move = El_Tetris(gamingArea, currentShape);
            if (move != null) {
                if (!currentShape.equals(move.shape)) {
                    super.tick(ROTATE);
                } else if (currentX > move.x) {
                    super.tick(LEFT);
                } else if (currentX < move.x) {
                    super.tick(RIGHT);
                } else {
                    super.tick(DOWN);
                }
            }
        }
    }

    @Override
    public Shape pickNextShape() {
        /*if (AIButton.isSelected()) {
            Shape AI_pick = new Shape(Shape.O_STR);
            int best_score = 0;
            for (Shape AI_shape : shapes) {
                gamingArea.undo();
                Move move = calculateBestMove(gamingArea, AI_shape);
                if (move == null) {
                    return AI_shape;
                } else if (move.score > best_score) {
                    best_score = (int) move.score;
                    AI_pick = AI_shape;
                }
            }
            return AI_pick;
        }*/
        return super.pickNextShape();
    }

    // 选择下一个图形：一般难度
    private Shape pickNormalShape(Shape[] shapes, Random random) {
        Shape shape = randShape(shapes, random);
        return randRotateShape(shape, random);
    }

    // 选择下一个图形：困难难度
    private Shape pickDifficultShape(Shape[] shapes, Random random) {
        Shape shape = randShape(shapes, random);

        int random_shape = 5;
        while ( random_shape-- > 0) {
            if (shape.equals(new Shape(Shape.S_STR)) || shape.equals(new Shape(Shape.Z_STR)) || shape.equals(new Shape(Shape.T_STR))) {
                return randRotateShape(shape, random);
            }
        }
        return randRotateShape(shape, random);
    }

    // 随机生成一个形状
    private Shape randShape(Shape[] shapes, Random random) {
        int shapeIndex = (int) (shapes.length * random.nextDouble());
        return shapes[shapeIndex];
    }

    // 随机旋转一个形状
    private Shape randRotateShape(Shape shape, Random random) {
        for (int i = 0; i < (int) (random.nextDouble() * 4); i++) {
            shape = shape.fastRotation();
        }
        return shape;
    }
}
