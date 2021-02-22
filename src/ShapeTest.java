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

    private Shape shape_1;
    private Shape shape_2;
    private Shape shape_3;
    private Shape shape_5;
    private Shape shape_6;

    @BeforeClass
    public static void init() {
        shapes = Shape.getShapes();
    }

    @Before
    public void setUp() {
        i=new Shape("0 0  0 1  0 2  0 3");
        l=new Shape("0 0  0 1  0 2  1 0");
        j=new Shape("0 0  1 0  1 1  1 2");
        s=new Shape("0 0  1 0  1 1  2 1");
        z=new Shape("0 1  1 1  1 0  2 0");
        o=new Shape("0 0  0 1  1 0  1 1");
        t=new Shape("0 0  1 0  1 1  2 0");
        shape_1=new Shape("0 0");
        shape_2=new Shape("0 0  1 1");
        shape_3=new Shape("0 0  1 1  2 1");
        shape_5=new Shape("0 0  1 1  2 1  1 2  1 0");
        shape_6=new Shape("0 1  1 1  2 1  1 2  1 0  1 3");
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
        Shape t1 = new Shape("1 5  2 4  3 3  4 2  1 2  3 2");
        Shape t2 = new Shape("1 5  2 4  3 3");

        Point[] points = t1.getPoints();
        assertTrue(Arrays.asList(points).contains(new Point(1, 5)));
        assertTrue(Arrays.asList(points).contains(new Point(2, 4)));
        assertTrue(Arrays.asList(points).contains(new Point(3, 3)));
        assertTrue(Arrays.asList(points).contains(new Point(4, 2)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 2)));
        assertTrue(Arrays.asList(points).contains(new Point(3, 2)));

        assertEquals(5, t1.getWidth());
        assertEquals(6, t1.getHeight());

        assertEquals(6, points.length);
        assertEquals(3, t2.getPoints().length);

        assertEquals(1,shape_1.getWidth());
        assertEquals(2,shape_2.getWidth());
        assertEquals(3,shape_3.getWidth());
        assertEquals(3,shape_5.getWidth());
        assertEquals(3,shape_6.getWidth());

        assertEquals(1,shape_1.getHeight());
        assertEquals(2,shape_2.getHeight());
        assertEquals(2,shape_3.getHeight());
        assertEquals(3,shape_5.getHeight());
        assertEquals(4,shape_6.getHeight());
    }

    @Test(timeout = 100)
    public void rotateCounterclockwiseTest() {
        for (Shape t : shapes) {
            Shape t1 = t.rotateCounterclockwise();
            Shape t2 = t1.rotateCounterclockwise();
            Shape t3 = t2.rotateCounterclockwise();
            Shape t4 = t3.rotateCounterclockwise();
            Shape t5 = t4.rotateCounterclockwise();

            assertEquals(t, t4);
            assertEquals(t5, t1);
        }

        Shape t1 = t.rotateCounterclockwise();
        Point[] points = t1.getPoints();
        assertEquals(4, points.length);
        assertTrue(Arrays.asList(points).contains(new Point(0, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 0)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 2)));

        Shape o1 = o.rotateCounterclockwise();
        Shape o2 = o1.rotateCounterclockwise();
        Shape o3 = o2.rotateCounterclockwise();
        assertEquals(o1, o);
        assertEquals(o2, o);
        assertEquals(o3, o);

        Shape s1 = s.rotateCounterclockwise();
        Shape s2 = s1.rotateCounterclockwise();
        assertEquals(s2, s);

        Shape diy = new Shape("0 0  1 0  1 1  2 0  2 1");
        Shape diy2 = diy.rotateCounterclockwise();
        points = diy2.getPoints();
        assertEquals(5, points.length);
        assertTrue(Arrays.asList(points).contains(new Point(0, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(0, 2)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 0)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(points).contains(new Point(1, 2)));
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

        assertNotNull(shapes);

        for (Shape shape : shapes) {
            Shape next = shape.fastRotation();
            assertNotNull(next);
            if (o.equals(shape)) {
                assertSame(shape, next);
                continue;
            }

            next = next.fastRotation();
            assertNotNull(next);
            if (i.equals(shape) || s.equals(shape) || z.equals(shape)) {
                assertSame(shape, next);
                continue;
            }

            next = next.fastRotation();
            assertNotNull(next);
            assertNotEquals(shape, next);

            next = next.fastRotation();
            assertNotNull(next);
            assertSame(shape, next);
        }
    }
}