import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AI {

    // 根据特征点，计算分数
    static double calcScore(int landingHeight, int rowsEliminated, int rowsTransition, int columnTransition, int numOfHoles, int wellSum) {
        return (-4.500158825082766 * landingHeight) +
                (3.4181268101392694 * rowsEliminated) +
                (-3.2178882868487753 * rowsTransition) +
                (-9.348695305445199 * columnTransition) +
                (-7.899265427351652 * numOfHoles) +
                (-3.3855972247263626 * wellSum);
    }

    /**
     * 根据shape和area计算最佳选择
     *
     * @param gamingArea
     * @param shape
     * @return 包含最佳的shape，x，y
     */
    // 计算图形最佳旋转度，最佳落点
    // El-Tetris算法
    default Move El_Tetris(GamingArea gamingArea, Shape shape) {
        Map<Double, Move> moveMap = new HashMap<>();

        tryPlace(gamingArea, shape, moveMap);

        Shape copy = shape_backup(shape);
        while (!copy.equals(shape.fastRotation())) {// 尝试每一种旋转角度
            shape = shape.fastRotation();
            tryPlace(gamingArea, shape, moveMap);
        }

        double maxScore = moveMap.keySet().stream().mapToDouble(score -> score).max().orElse(Double.MIN_VALUE);
        if (maxScore != Double.MIN_VALUE) {
            return moveMap.get(maxScore);
        }
        return null;
    }

    //备份未旋转的形状
    default Shape shape_backup(Shape shape) {
        int len = shape.getPoints().length;
        Point[] points = new Point[len];
        for (int i = 0; i < len; i++) {
            points[i] = shape.getPoints()[i];
        }
        return new Shape(points);
    }

    // 在每一列中尝试放置当前图形
    default void tryPlace(GamingArea gamingArea, Shape shape, Map<Double, Move> moveMap) {
        for (int col = 0; col < gamingArea.getAreaWidth() - shape.getWidth() + 1; col++) {// 尝试每一个位置
            int y = gamingArea.getDropHeight(shape, col);
            Move move = new Move();
            move.x = col;
            move.y = y;
            move.shape = shape;

            gamingArea.undo();
            int result = gamingArea.place(shape, col, y);
            if (result <= GamingArea.ROW_FULL) { // 放置成功
                // 方块放置之后的高度
                int landingHeight = gamingArea.getColumnHeight(col) + (shape.getHeight() + 1) / 2;
                // 方块放置之后,消行层数与当前方块贡献出的方格数乘积
                int rowsEliminated = gamingArea.clearRows();
                //方块放置之后，水平方向上方块的变换次数
                int rowsTransition = RowTransition(gamingArea);
                //方块放置之后，垂直方向上方块的变换次数
                int columnTransition = ColumnTransitions(gamingArea);
                //方块放置之后，空洞的个数
                int numOfHoles = NumberOfHoles(gamingArea);
                // 方块放置后，"井"深的"连加和"(每一个井深加到1的结果之后)
                int wellSum = Bumpiness(gamingArea);

                double score = calcScore(landingHeight, rowsEliminated, rowsTransition, columnTransition, numOfHoles, wellSum);
                move.score = score;
                moveMap.put(score, move);
            }
        }
        gamingArea.undo();
    }

    // 这个人工智能其实是个人工智障，不过用来说明问题已经足够了
    default Move calculateBestMove(GamingArea gamingArea, Shape shape) {
        // 计算最佳旋转
        if (shape.getHeight() > shape.fastRotation().getHeight()) {
            shape = shape.fastRotation();
        }

        // 计算最佳位置
        int bestCol = 0, bestRow = 0, bestScore = 99999999;
        boolean moveFound = false;
        for (int col = 0; col < gamingArea.getAreaWidth(); col++) {
            int row = gamingArea.getColumnHeight(col);
            int result = gamingArea.place(shape, col, row);
            if (result <= GamingArea.ROW_FULL) {
                int score = row + shape.getHeight();
                if (!moveFound) {
                    bestCol = col;
                    bestRow = row;
                    bestScore = score;
                    moveFound = true;
                } else if (score < bestScore) {
                    bestCol = col;
                    bestRow = row;
                    bestScore = score;
                }
            }
            gamingArea.undo();
        }

        // 返回计算出的bestMove
        if (!moveFound) {
            return null;
        } else {
            Move bestMove = new Move();
            bestMove.shape = shape;
            bestMove.x = bestCol;
            bestMove.y = bestRow;
            bestMove.score = bestScore;
            return bestMove;
        }
    }

    /***
     * 井深累加和，计算所以井的井深，进行累加和，2,3-》（1+2）+（1+2+3）；
     * @param gamingArea
     * @return
     */
    default int Bumpiness(GamingArea gamingArea) {

        List<Integer> Bumpiness = new ArrayList<>();
        for (int x = 1; x < gamingArea.getAreaWidth() - 1; x++) {
            int row = 0;
            int num = 0;
            while (row < gamingArea.getAreaHeight()) {
                if (!gamingArea.isFilled(x, row) && gamingArea.isFilled(x - 1, row) && gamingArea.isFilled(x + 1, row)) {
                    num++;
                } else if (num > 0) {
                    Bumpiness.add(num);
                    num = 0;
                }
                row++;
            }
            Bumpiness.add(num);
        }
        return Bumpiness.stream().mapToInt(this::sumX).sum();
    }

    //累加
    default int sumX(int num) {
        int sum = 0;
        while (num > 0) {
            sum += num;
            num--;
        }
        return sum;
    }

    /**
     * 空洞数，每列顶部被砖块封住，则下部空位为空洞
     *
     * @param gamingArea
     * @return
     */
    default int NumberOfHoles(GamingArea gamingArea) {

        int numberOfHoles = 0;
        for (int x = 0; x < gamingArea.getAreaWidth(); x++) {
            boolean start = false;
            for (int y = gamingArea.getAreaHeight() - 1; y >= 0; y--) {
                if (gamingArea.isFilled(x, y)) {
                    start = true;
                } else if (!gamingArea.isFilled(x, y) && start) {
                    numberOfHoles++;
                }
            }
        }
        return numberOfHoles;
    }

    /**
     * 纵向变化程度，区域外视为有砖块，有-》无，无-》有，都算变化一次
     *
     * @param gamingArea
     * @return
     */
    default int ColumnTransitions(GamingArea gamingArea) {

        int columnTransitions = 0;
        for (int x = 0; x < gamingArea.getAreaWidth(); x++) {
            for (int y = 0; y <= gamingArea.getAreaHeight(); y++) {
                if (gamingArea.isFilled(x, y - 1) != gamingArea.isFilled(x, y)) {
                    columnTransitions++;
                }
            }
        }
        return columnTransitions;
    }

    /**
     * 横向变化程度，区域外视为有砖块，有-》无，无-》有，都算变化一次
     *
     * @param gamingArea
     * @return
     */
    default int RowTransition(GamingArea gamingArea) {
        int rowTransition = 0;
        for (int y = 0; y < gamingArea.getAreaHeight(); y++) {
            for (int x = 0; x <= gamingArea.getAreaWidth(); x++) {
                if (gamingArea.isFilled(x - 1, y) != gamingArea.isFilled(x, y)) {
                    rowTransition++;
                }
            }
        }
        return rowTransition;
    }

    class Move {
        public int x;
        public int y;
        public Shape shape;
        public double score;    // 分数越高越好
    }
}