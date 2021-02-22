import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ShapeTest {
    private static Shape[] shapes;
    private Shape t;
    private Shape l;
    private Shape j;
    private Shape s;
    private Shape i;
    private Shape z;
    private Shape o;

    @BeforeClass
    public static void init() {
        shapes = Shape.getShapes();
    }

    @Before
    public void setUp() {
        t = new Shape(Shape.T_STR);
        l = new Shape(Shape.L_STR);
        j = new Shape(Shape.J_STR);
        s = new Shape(Shape.S_STR);
        i = new Shape(Shape.I_STR);
        z = new Shape(Shape.Z_STR);
        o = new Shape(Shape.O_STR);
    }

    @Ignore
    @Test(timeout = 100)
    public void stupidTest() {
        // 形状T
        assertEquals(4, t.getPoints().length);
        assertTrue(Arrays.asList(t.getPoints()).contains(new Point(1, 0)));

        // 形状L
        Shape l = new Shape(Shape.L_STR);
        assertTrue(Arrays.asList(l.getPoints()).contains(new Point(1, 0)));
    }

    @Test(timeout = 100)
    public void shapeTest() {
        Shape j1 = new Shape("1 0  2 0  2 1  2 2  2 3");
        assertNotNull(j1);
        assertEquals(5, j1.getPoints().length);

        Point[] j1Points = j1.getPoints();
        assertTrue(Arrays.asList(j1Points).contains(new Point(0, 0)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(1, 0)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(1, 2)));

        assertEquals(2, j1.getWidth());
        assertEquals(4, j1.getHeight());

        assertEquals(3, t.getWidth());
        assertEquals(2, l.getWidth());
        assertEquals(2, j.getWidth());
        assertEquals(3, s.getWidth());
        assertEquals(1, i.getWidth());
        assertEquals(3, z.getWidth());
        assertEquals(2, o.getWidth());

        assertEquals(4, i.getHeight());
        assertEquals(3, l.getHeight());
        assertEquals(3, j.getHeight());
        assertEquals(2, s.getHeight());
        assertEquals(2, z.getHeight());
        assertEquals(2, o.getHeight());
        assertEquals(2, t.getHeight());
    }

    @Test(timeout = 100)
    public void rotateCounterclockwiseTest() {
        Shape j1 = j.rotateCounterclockwise();
        Shape j2 = j1.rotateCounterclockwise();
        Shape j3 = j2.rotateCounterclockwise();
        Shape j4 = j3.rotateCounterclockwise();

        assertNotNull(j1);
        assertNotNull(j2);
        assertNotNull(j3);
        assertNotNull(j4);

        assertNotSame(j1, j);
        assertNotSame(j2, j);
        assertNotSame(j3, j);
        assertSame(j4, j);

        Point[] j1Points = j1.getPoints();
        assertTrue(Arrays.asList(j1Points).contains(new Point(0, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(2, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(2, 0)));
    }

    @Test(timeout = 100)
    public void equalsTest() {
        assertEquals(i, i);

        assertNotEquals(i, l);
        assertNotEquals(i, null);
        assertNotEquals(null, i);

        Shape s2 = new Shape("2 1  0 0  1 0  1 1");
        Shape s3 = new Shape("2 1  1 0  1 1  0 0");
        assertEquals(s, s2);
        assertEquals(s2, s);
        assertEquals(s2, s3);
        assertEquals(s, s3);

        Shape o2 = new Shape("0 0  0 1  1 0  1 1  5 0");
        assertNotEquals(o, o2);
        assertNotEquals(o2, o);
    }

    @Test(timeout = 100)
    public void makeFastRotationsTest() {

        Shape[] shapes = Shape.getShapes();
        for (Shape t : shapes) {
            Shape t1 = t.fastRotation();
            Shape t2 = t1.fastRotation();
            Shape t3 = t2.fastRotation();
            Shape t4 = t3.fastRotation();
            assertTrue(t == t4);
        }


        Shape t1 = shapes[6];
        assertEquals(t1, t);

        Shape t2 = t1.fastRotation();
        Shape t3 = t2.fastRotation();
        Shape t4 = t3.fastRotation();
        Shape t5 = t4.fastRotation();

        assertNotNull(t2);
        assertNotNull(t3);
        assertNotNull(t4);
        assertNotNull(t5);
        assertNotEquals(t, t2);
        assertNotEquals(t, t3);
        assertNotEquals(t, t4);

        Shape o1 = shapes[5];
        Shape o2 = o1.fastRotation();
        assertEquals(o1, o2);
        assertTrue(o1 == o2);

        Shape s1 = shapes[3];
        Shape s2 = s1.fastRotation();
        Shape s3 = s2.fastRotation();
        assertSame(s1,s3);

        Shape l1 = shapes[0];
        Shape l2 = l1.fastRotation();
        Shape l3 = l2.fastRotation();
        assertNotEquals(l1, l2);
        assertTrue(l1 == l3);
    }
}