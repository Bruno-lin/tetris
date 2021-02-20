import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ShapeTest {
    private Shape t;
    private Shape l;
    private Shape j;
    private Shape s;
    private Shape i;
    private Shape z;
    private Shape o;

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
        assertNotNull(j);
        assertEquals(4,j.getPoints().length);

        Point[] jPoints= j.getPoints();
        assertTrue(Arrays.asList(jPoints).contains(new Point(0, 0)));
        assertTrue(Arrays.asList(jPoints).contains(new Point(1, 0)));
        assertTrue(Arrays.asList(jPoints).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(jPoints).contains(new Point(1, 2)));

        assertEquals(2, j.getWidth());
        assertEquals(3, j.getHeight());
    }

    @Test(timeout = 100)
    public void rotateCounterclockwiseTest() {
        Shape j1=j.rotateCounterclockwise();
        Shape j2=j1.rotateCounterclockwise();
        Shape j3=j2.rotateCounterclockwise();
        Shape j4=j3.rotateCounterclockwise();

        assertNotNull(j1);
        assertNotNull(j2);
        assertNotNull(j3);
        assertNotNull(j4);

        assertNotSame(j1,j);
        assertNotSame(j2,j);
        assertNotSame(j3,j);
        assertSame(j4,j);

        Point[] j1Points= j1.getPoints();
        assertTrue(Arrays.asList(j1Points).contains(new Point(0, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(1, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(2, 1)));
        assertTrue(Arrays.asList(j1Points).contains(new Point(2, 0)));
    }

    @Test(timeout = 100)
    public void equalsTest() {
        assertEquals(i,i);

        assertNotEquals(i,l);
        assertNotEquals(i,null);
        assertNotEquals(null,i);

        Shape s2=new Shape("2 1  0 0  1 0  1 1");
        Shape s3=new Shape("2 1  1 0  1 1  0 0");
        assertEquals(s,s2);
        assertEquals(s2,s);
        assertEquals(s2,s3);
        assertEquals(s,s3);

        Shape o2=new Shape("0 0  0 1  1 0  1 1  5 0");
        assertNotEquals(o,o2);
        assertNotEquals(o2,o);
    }

    @Test(timeout = 100)
    public void makeFastRotationsTest() {
        Shape[] shapes = Shape.getShapes();
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
        assertNotEquals(t1, t2);
        assertEquals(t5, t1);

        Shape o = new Shape("0 0  0 1  1 0  1 1");
        Shape o1 = o.rotateCounterclockwise();
        Shape o2 = shapes[5].fastRotation();

        assertEquals(o, o1);
        assertEquals(o1, o2);
        assertEquals(o, o2);
    }
}