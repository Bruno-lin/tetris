import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏区域，本质上是一个boolean型二维数组，一些地方有方块、另一些地方是空白
 */
public class GamingArea {
    public static final int OK = 0;
    public static final int ROW_FULL = 1;
    public static final int OUT = 2;
    public static final int COLLIDED = 3;

    boolean committed;  // 放置状态
    private int width;
    private int height;

    private boolean[][] board_cache;  // 未提交的游戏区状态
    private boolean[][] board;    // 已提交的游戏区状态

    public GamingArea(int width, int height) {
        this.width = Math.abs(width); //防止传入负值
        this.height = Math.abs(height); //防止传入负值
        board_cache = new boolean[this.width][this.height];
        board = new boolean[this.width][this.height];
        committed = true;
    }

    /**
     * 返回游戏区域的宽度（非像素，而是横着可以放多少方块）
     */
    public int getAreaWidth() {
        return width;
    }

    /**
     * 返回游戏区域的高度（非像素，而是竖着可以放多少方块）
     */
    public int getAreaHeight() {
        return height;
    }

    /**
     * 整个游戏区域中已经放置的最高方块的位置
     */
    public int getMaxHeight() {
        for (int yMax = height - 1; yMax >= 0; yMax--) {
            for (int x = 0; x < width; x++) {
                if (board_cache[x][yMax]) {
                    return yMax + 1;
                }
            }
        }
        return 0;
    }


    /**
     * 基于当前的游戏区格局，计算当用户按下掉落键（n）时，某一形状应该下降到的高度
     *
     * @param shape 即将掉落的形状
     * @param col   该形状目前所在的位置（仅x坐标，即列）
     * @return 掉落后，该形状理应到达的高度（仅y坐标，即行）
     */
    public int getDropHeight(Shape shape, int col) {

        if (col < 0 || col >= width) {
            return 0;
        }

        int shape_width = shape.getWidth();
        // 获取当前方块与已放置方块的最短距离
        Point[] points = convert_to_coordinate(shape, 0, 0);
        // 把每col的坐标记录在HashMap里面
        Map<Integer, List<Point>> points_recorded_by_x = new HashMap<>();
        for (Point point : points) {
            points_recorded_by_x.computeIfAbsent(point.x, k -> new ArrayList<>()).add(point);
        }
        // 新建一个来存储形状的每个方块与各个col底部图形的距离
        int[] instance_to_bot = new int[shape_width];
        // 算出每个col中位于该col的形状block与底部图形的距离
        for (int i = col; i < col + shape_width; i++) {
            int max = getCommittedColumnHeight(i);
            int x_shape = i - col;
            int minY = points_recorded_by_x.get(x_shape).stream().map(point -> point.y).mapToInt(y -> y).min().orElse(0);
            instance_to_bot[x_shape] = max - minY;
        }
        // 掉落后，该形状理应到达的高度(y轴坐标)
        boolean seen = false;
        int result = 0;
        for (int y : instance_to_bot) {
            if (!seen || y > result) {
                seen = true;
                result = y;
            }
        }
        return seen ? result : 0;
    }

    /**
     * 游戏中干净区域某一列的当前高度
     *
     * @param col
     * @return
     */
    private int getCommittedColumnHeight(int col) {
        for (int i = height - 1; i >= 0; i--) {
            if (board[col][i]) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * 返回某一列的当前高度。如果没方块则高度是0。
     * 注意：如果某一列有空心的情况，高度应以位置最高的方块为准。
     */
    public int getColumnHeight(int col) {
        if (col < 0 || col >= width)
            return 0;
        for (int y = height - 1; y >= 0; y--) {
            if (board_cache[col][y]) {
                return y + 1;
            }
        }
        return 0;
    }

    /**
     * 某一行已经填上方块的个数
     */
    public int getFilledBlockCount(int row) {
        int filled_block_cnt = 0;
        if (row < 0 || row >= height)
            return 0;
        for (int x = 0; x < width; x++) {
            if (board_cache[x][row]) {
                filled_block_cnt += 1;
            }
        }
        return filled_block_cnt;
    }

    /**
     * 检查游戏区域的某个坐标是否已经放置有方块。
     * 如果坐标超出边界，返回true即可。
     */
    public boolean isFilled(int col, int row) {
        if (col < 0 || row < 0 || col >= width || row >= height) {
            return true;
        }
        return board_cache[col][row];
    }

    /**
     * 尝试将形状放置在/调整到游戏区域某个位置，可能有几种不同的结果：
     * <p>
     * - 正常放置，无特殊事情发生，返回OK
     * - 放置后导致某一行或几行填满，可以消除，ROW_FILLED
     * - 形状超出边界，操作无效，返回OUT
     * - 和现有的方块重叠，操作无效，返回COLLIDED
     * <p>
     * 对于后两种情况，调用者后续应使用undo进行撤销。
     */
    public int place(Shape shape, int col, int row) {
        // sanity check
        if (!committed) {
            throw new RuntimeException("未commit时调用place");
        }
        committed = false;

        // 检查越界
        if (col < 0 || row < 0 || col + shape.getWidth() > width || row + shape.getHeight() > height) {
            return OUT;
        }

        // 放置
        Point[] points = convert_to_coordinate(shape, col, row);
        for (Point point : points) {
            board_cache[point.x][point.y] = true;
        }

        // 重叠检查
        for (Point point : points) {
            if (board[point.x][point.y]) {
                return COLLIDED;
            }
        }

        // 检查是否可消除
        for (int i = row; i < row + shape.getHeight(); i++) {
            if (getFilledBlockCount(row) == width) {
                return ROW_FULL;
            }
        }

        // 正常放置
        return OK;
    }


    /**
     * 消除填满的行，更新游戏区，并返回消除的行数
     */
    public int clearRows() {
        int rowsCleared = 0;
        int[] delRows = new int[height];
        // 先清除
        for (int area_y = 0; area_y < height; area_y++) {
            if (getFilledBlockCount(area_y) == width) {
                rowsCleared++;
                for (int area_x = 0; area_x < width; area_x++) {
                    board_cache[area_x][area_y] = false;
                }
                delRows[area_y] = 1;
            }
        }
        // 后移动
        for (int row = height - 1; row >= 0; row--) {
            if (delRows[row] == 1) {
                for (int move_down_y = row; move_down_y < height - 1; move_down_y++) {
                    for (int move_down_x = 0; move_down_x < width; move_down_x++) {
                        board_cache[move_down_x][move_down_y] = board_cache[move_down_x][move_down_y + 1];
                    }
                }
            }
        }
        committed = false; // 已修改，未提交
        return rowsCleared;
    }


    /**
     * 撤销操作，恢复到放置前的状态
     */
    public void undo() {
        duplicate_board_status(board, board_cache);
        committed = true;
    }


    /**
     * 放置生效
     */
    public void commit() {
        duplicate_board_status(board_cache, board);
        committed = true;
    }

    /**
     * 复制游戏区状态数组
     */
    private void duplicate_board_status(boolean[][] source, boolean[][] target) {
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, target[i], 0, source[i].length);
        }
    }

    /**
     * 将图形中的点的坐标转换为游戏区域中的坐标
     */
    private Point[] convert_to_coordinate(Shape shape, int col, int row) {
        Point[] points = shape.getPoints();
        Point[] points_temp = new Point[points.length];

        //找出形状最小的x轴
        boolean seen_x = false;
        int result_z = 0;
        for (Point point1 : points) {
            int x = point1.x;
            if (!seen_x || x < result_z) {
                seen_x = true;
                result_z = x;
            }
        }
        int x_min = seen_x ? result_z : 0;
        //找出形状最小的x轴
        boolean seen_y = false;
        int result_y = 0;
        for (Point point : points) {
            int y = point.y;
            if (!seen_y || y < result_y) {
                seen_y = true;
                result_y = y;
            }
        }
        int y_min = seen_y ? result_y : 0;

        //算出在当前游戏区域里的(x,y)坐标，并返回
        for (int i = 0; i < points.length; i++) {
            points_temp[i] = new Point(points[i].x - x_min + col, points[i].y - y_min + row);
        }
        return points_temp;
    }


    /**
     * 将当前游戏区域转换成一个字符串，可以方便debug
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        // 当前的游戏区域
        for (int row = height - 1; row >= 0; row--) {
            buffer.append('|');
            for (int col = 0; col < width; col++) {
                if (isFilled(col, row)) {
                    buffer.append('+');
                } else {
                    buffer.append(' ');
                }
            }
            buffer.append("|\n");
        }

        // 分割线
        buffer.append("-".repeat(width + 2));
        buffer.append('\n');

        return buffer.toString();
    }
}


