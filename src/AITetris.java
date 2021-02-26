public class AITetris extends Tetris implements AI {

//    JCheckBox debug

    AITetris(int pixels) {
        super(pixels);
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
