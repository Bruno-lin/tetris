import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShapeTest {
    private Shape t;
    private Shape l;

    @Before
    public void setUp() {
        t = new Shape(Shape.T_STR);
        l = new Shape(Shape.L_STR);
    }

    @Test
    public void stupidTest() {
        // 形状T
        assertEquals(4, t.getPoints().length);
        assertTrue(Arrays.asList(t.getPoints()).contains(new Point(1, 0)));

        // 形状L
        assertEquals(4, l.getPoints().length);
        assertTrue(Arrays.asList(l.getPoints()).contains(new Point(1, 0)));
    }
}