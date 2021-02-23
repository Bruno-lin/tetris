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
        Point[] points=i.getPoints();
        assertNotNull(points);
        assertEquals(4,points.length);

        assertTrue(Arrays.asList(points).contains(new Point(0,0)));
        assertTrue(Arrays.asList(points).contains(new Point(0,1)));
        assertTrue(Arrays.asList(points).contains(new Point(0,2)));
        assertTrue(Arrays.asList(points).contains(new Point(0,3)));

        assertEquals(1,shape_1.getPoints().length);
        assertEquals(2,shape_2.getPoints().length);
        assertEquals(3,shape_3.getPoints().length);
        assertEquals(5,shape_5.getPoints().length);
        assertEquals(6,shape_6.getPoints().length);

        assertTrue(Arrays.asList(shape_1.getPoints()).contains(new Point(0,0)));

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
        Shape o_new_1=o.rotateCounterclockwise();
        Shape o_new_2=o_new_1.rotateCounterclockwise();
        Shape o_new_3=o_new_2.rotateCounterclockwise();
        assertEquals(o,o_new_1);
        assertEquals(o_new_1,o_new_2);
        assertEquals(o_new_2,o_new_3);
        assertEquals(o,o_new_3);

        Shape l_new_1=l.rotateCounterclockwise();
        Shape l_new_2=l_new_1.rotateCounterclockwise();
        Shape l_new_3=l_new_2.rotateCounterclockwise();
        Shape l_new_4=l_new_3.rotateCounterclockwise();
        Shape l_new_5=l_new_4.rotateCounterclockwise();
        assertNotEquals(l,l_new_1);
        assertNotEquals(l,l_new_2);
        assertNotEquals(l,l_new_3);
        assertEquals(l,l_new_4);
        assertEquals(l_new_1,l_new_5);

        Shape s_new_1=s.rotateCounterclockwise();
        Shape s_new_2=s_new_1.rotateCounterclockwise();
        Shape s_new_3=s_new_2.rotateCounterclockwise();
        assertNotEquals(s,s_new_1);
        assertEquals(s,s_new_2);
        assertEquals(s_new_1,s_new_3);
        assertNotEquals(s_new_1,s_new_2);

        Shape t=shapes[6];
        Shape t_new_1=t.rotateCounterclockwise();
        assertNotNull(t_new_1);
        assertNotSame(t,t_new_1);
        assertTrue(Arrays.asList(t_new_1.getPoints()).contains(new Point(1,0)));
        assertTrue(Arrays.asList(t_new_1.getPoints()).contains(new Point(1,1)));
        assertTrue(Arrays.asList(t_new_1.getPoints()).contains(new Point(1,2)));
        assertTrue(Arrays.asList(t_new_1.getPoints()).contains(new Point(0,1)));

        Shape t_new_2=t_new_1.rotateCounterclockwise();
        assertNotNull(t_new_2);
        assertNotSame(t,t_new_2);
        assertTrue(Arrays.asList(t_new_2.getPoints()).contains(new Point(2,1)));
        assertTrue(Arrays.asList(t_new_2.getPoints()).contains(new Point(1,1)));
        assertTrue(Arrays.asList(t_new_2.getPoints()).contains(new Point(0,1)));
        assertTrue(Arrays.asList(t_new_2.getPoints()).contains(new Point(1,0)));

        Shape t_new_3=t_new_2.rotateCounterclockwise();
        assertNotNull(t_new_3);
        assertNotSame(t,t_new_3);
        assertTrue(Arrays.asList(t_new_3.getPoints()).contains(new Point(0,2)));
        assertTrue(Arrays.asList(t_new_3.getPoints()).contains(new Point(0,1)));
        assertTrue(Arrays.asList(t_new_3.getPoints()).contains(new Point(0,0)));
        assertTrue(Arrays.asList(t_new_3.getPoints()).contains(new Point(1,1)));

        Shape t_new_4=t_new_3.rotateCounterclockwise();
        assertNotNull(t_new_4);
        assertNotSame(t,t_new_4);
        assertTrue(Arrays.asList(t_new_4.getPoints()).contains(new Point(0,0)));
        assertTrue(Arrays.asList(t_new_4.getPoints()).contains(new Point(1,0)));
        assertTrue(Arrays.asList(t_new_4.getPoints()).contains(new Point(2,0)));
        assertTrue(Arrays.asList(t_new_4.getPoints()).contains(new Point(1,1)));

        Shape shape_new=shape_6.rotateCounterclockwise();
        assertNotNull(shape_new);
        assertNotSame(shape_6,shape_new);
        assertTrue(Arrays.asList(shape_new.getPoints()).contains(new Point(2,0)));
        assertTrue(Arrays.asList(shape_new.getPoints()).contains(new Point(2,1)));
        assertTrue(Arrays.asList(shape_new.getPoints()).contains(new Point(2,2)));
        assertTrue(Arrays.asList(shape_new.getPoints()).contains(new Point(1,1)));
        assertTrue(Arrays.asList(shape_new.getPoints()).contains(new Point(3,1)));
        assertTrue(Arrays.asList(shape_new.getPoints()).contains(new Point(0,1)));
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