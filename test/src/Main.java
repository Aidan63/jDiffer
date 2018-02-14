package src;

import uk.aidanlee.jDiffer.data.ShapeCollision;
import uk.aidanlee.jDiffer.math.Vector;
import uk.aidanlee.jDiffer.shapes.Polygon;
import uk.aidanlee.jDiffer.Collision;

public class Main {
    public static void main(String[] _args) {
        Polygon poly1 = new Polygon(0, 0, new Vector[] {
                new Vector(32, 32),
                new Vector(64, 32),
                new Vector(32, 64),
                new Vector(64, 64)
        });
        Polygon poly2 = new Polygon(0, 0, new Vector[] {
                new Vector(48, 48),
                new Vector(80, 48),
                new Vector(48, 80),
                new Vector(80, 80)
        });

        ShapeCollision collision = Collision.shapeWithShape(poly1, poly2);
        System.out.println(collision.separationX + ":" + collision.separationY);
    }
}
