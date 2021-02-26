import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class GamingAreaTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(100);

    private int width = 10;
    private int height = 24;

    private GamingArea area;
    private GamingArea area2;

    private Shape shape1_1;
    private Shape shape1_2;
    private Shape shape4_1;
    private Shape shape4_2;
    private Shape shapeT_1;
    private Shape shapeT_2;
    private Shape shapeT_3;
    private Shape shape10_1;
    private Shape shape10_2;

    @Before
    public void setUp() {
        area = new GamingArea(width, height);
        area2 = new GamingArea(-width, -height);
        shape1_1 = new Shape("0 0");
        shape1_2 = new Shape("1 1");
        shape4_1 = new Shape("0 0  0 1  0 2  0 3");
        shape4_2 = new Shape("0 0  1 0  2 0  3 0");
        shape10_1 = new Shape("0 0  0 1  0 2  0 3  0 4  0 5  0 6  0 7  0 8  0 9");
        shape10_2 = new Shape("0 0  1 0  2 0  3 0  4 0  5 0  6 0  7 0  8 0  9 0");
        shapeT_1 = new Shape("0 0  1 0  2 0 1 1");
        shapeT_2 = new Shape("0 0  0 1  0 2 1 1");
        shapeT_3 = new Shape("0 1  1 1  1 2 1 0");
    }

    @Test
    public void testGetAreaWidth() {
        assertEquals(width, area.getAreaWidth());
        assertEquals(width, area2.getAreaWidth());
    }

    @Test
    public void testGetAreaHeight() {
        assertEquals(height, area.getAreaHeight());
        assertEquals(height, area2.getAreaHeight());
    }

    @Test
    public void testGetMaxHeight() {
        assertTrue(area.getMaxHeight() >= 0 && area.getMaxHeight() <= height);
        assertEquals(0, area.getMaxHeight());

        area.committed = true;
        area.place(shape1_1, 0, 0);
        area.committed = true;
        area.place(shape4_1, 1, 0);
        area.committed = true;
        area.place(shape4_2, 5, 2);
        assertEquals(4, area.getMaxHeight());
    }

    @Test
    public void testGetColumnHeight() {
        for (int i = 0; i < width; i++) {
            assertTrue(area.getColumnHeight(i) >= 0 && area.getColumnHeight(i) <= height);
            assertEquals(0, area.getColumnHeight(i));
        }

        assertEquals(0, area.getColumnHeight(-1));
        assertEquals(0, area.getColumnHeight(width));

        area.committed = true;
        area.place(shape1_1, 0, 0);
        assertEquals(1, area.getColumnHeight(0));

        area.committed = true;
        area.place(shape4_2, 0, 2);
        assertEquals(3, area.getColumnHeight(0));
        assertEquals(3, area.getColumnHeight(1));
    }

    @Test
    public void testGetDropHeight() {
        assertEquals(0, area.getDropHeight(shape1_1, -1));
        assertEquals(0, area.getDropHeight(shape1_1, width));
        assertEquals(0, area.getDropHeight(shape1_1, 0));
        assertEquals(0, area.getDropHeight(shape1_2, 0));

        area = new GamingArea(width, height);
        area.commit();
        area.place(shape1_1, 0, 0);
        area.commit();
        assertEquals(1, area.getDropHeight(shape1_1, 0));
        area.commit();
        area.place(shape1_1, 0, 5);
        area.commit();
        assertEquals(6, area.getDropHeight(shape1_1, 0));
        area.commit();
        area.place(shape1_1, 0, 3);
        area.commit();
        assertEquals(6, area.getDropHeight(shape1_1, 0));
        area.commit();
        area.place(shape1_1, 0, height - 1);
        area.commit();
        assertEquals(24, area.getDropHeight(shape1_1, 0));

        area = new GamingArea(width, height);
        area.commit();
        area.place(shapeT_1, 0, 0);
        area.commit();
        assertEquals(1, area.getDropHeight(shapeT_2, 0));

        area = new GamingArea(width, height);
        area.commit();
        area.place(shape1_1, width - 2, 3);
        area.commit();
        assertEquals(3, area.getDropHeight(shapeT_3, width - 2));
    }

    @Test
    public void testGetFilledBlockCount() {
        for (int i = 0; i < height; i++) {
            assertEquals(0, area.getFilledBlockCount(i));
            assertTrue(area.getFilledBlockCount(i) >= 0 && area.getFilledBlockCount(i) <= height);
        }
        assertEquals(0, area.getFilledBlockCount(-1));
        assertEquals(0, area.getFilledBlockCount(height));

        area.committed = true;
        area.place(shape4_1, 0, 0);
        assertEquals(1, area.getFilledBlockCount(0));
        area.committed = true;
        area.place(shape4_2, 2, 0);
        assertEquals(5, area.getFilledBlockCount(0));
    }

    @Test
    public void testIsFilled() {
        assertTrue(area.isFilled(-1, -1));
        assertTrue(area.isFilled(width, height));
        assertFalse(area.isFilled(0, 0));

        area.committed = true;
        area.place(shape1_1, 1, 1);
        assertTrue(area.isFilled(1, 1));
    }

    @Test
    public void testPlace() {
        area.commit();
        assertEquals(GamingArea.OUT, area.place(shape1_1, -1, -1));
        area.commit();
        assertEquals(GamingArea.OUT, area.place(shape1_1, width, height));
        area.commit();

        assertEquals(GamingArea.OK, area.place(shape1_1, 0, 0));
        area.commit();
        assertEquals(GamingArea.OK, area.place(shape1_2, 0, 1));
        area.commit();
        assertEquals(GamingArea.COLLIDED, area.place(shape1_1, 0, 0));
        area.commit();
        assertEquals(GamingArea.COLLIDED, area.place(shape1_2, 0, 0));
        area.commit();
        assertEquals(GamingArea.ROW_FULL, area.place(shape10_2, 0, 2));

        area.committed = true;
        area.place(shape1_1, 0, 0);
        assertFalse(area.committed);
    }

    @Test
    public void testClearRows() {
        assertEquals(0, area.clearRows());

        area.committed = true;
        area.place(shape1_1, 3, 0);
        area.committed = true;
        area.place(shape10_2, 0, 1);
        area.committed = true;
        area.place(shape1_1, 3, 2);
        assertEquals(3, area.getMaxHeight());
        assertEquals(1, area.clearRows());
        assertEquals(2, area.getMaxHeight());

        area.committed = true;
        area.place(shape10_2, 0, 3);
        area.committed = true;
        area.place(shape1_1, 0, 4);
        area.committed = true;
        area.place(shape10_2, 0, 5);
        assertEquals(6, area.getMaxHeight());
        assertEquals(2, area.clearRows());
        assertEquals(4, area.getMaxHeight());

        area.clearRows();
        assertFalse(area.committed);

        area = new GamingArea(width, height);
        for (int i = 0; i < height; i++) {
            area.commit();
            area.place(shape10_2, 0, i);
        }
        assertEquals(height, area.getMaxHeight());
        area.clearRows();
        assertEquals(0, area.getMaxHeight());

    }

    @Test
    public void testUndo() {
        area.committed = true;
        area.place(shape1_1, 0, 0);
        assertTrue(area.isFilled(0, 0));
        area.undo();
        assertFalse(area.isFilled(0, 0));
        assertTrue(area.committed);

        area.committed = true;
        area.place(shape10_2, 0, 0);
        area.commit();
        area.committed = true;
        area.place(shape10_2, 0, 1);
        area.committed = true;
        area.place(shape4_1, 0, 2);
        assertEquals(width, area.getFilledBlockCount(0));
        assertEquals(width, area.getFilledBlockCount(1));
        assertEquals(6, area.getMaxHeight());
        assertEquals(2, area.clearRows());
        assertEquals(1, area.getFilledBlockCount(0));
        assertEquals(4, area.getMaxHeight());
        area.undo();
        assertEquals(width, area.getFilledBlockCount(0));
        assertEquals(0, area.getFilledBlockCount(1));
        assertEquals(1, area.getMaxHeight());

        area = new GamingArea(width, height);
        area.committed = true;
        area.place(shape10_2, 0, 0);
        area.committed = true;
        area.place(shape1_1, 0, 1);
        area.committed = true;
        area.place(shape10_2, 0, 2);
        area.clearRows();
        area.undo();
        assertEquals(0, area.getMaxHeight());
    }

    @Test
    public void testCommit() {
        area.commit();
        assertTrue(area.committed);

        area.committed = true;
        area.place(shape10_2, 0, 0);
        area.committed = true;
        area.place(shape10_2, 0, 1);
        area.committed = true;
        area.place(shape4_1, 0, 2);
        assertEquals(width, area.getFilledBlockCount(0));
        assertEquals(width, area.getFilledBlockCount(1));
        assertEquals(6, area.getMaxHeight());
        area.clearRows();
        assertEquals(1, area.getFilledBlockCount(0));
        assertEquals(4, area.getMaxHeight());
        area.commit();
        assertEquals(1, area.getFilledBlockCount(0));
        assertEquals(4, area.getMaxHeight());
    }
}
