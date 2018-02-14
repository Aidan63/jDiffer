package uk.aidanlee.jDiffer;

import uk.aidanlee.jDiffer.data.RayCollision;
import uk.aidanlee.jDiffer.data.RayIntersection;
import uk.aidanlee.jDiffer.data.ShapeCollision;
import uk.aidanlee.jDiffer.math.Vector;
import uk.aidanlee.jDiffer.sat.SAT2D;
import uk.aidanlee.jDiffer.shapes.Polygon;
import uk.aidanlee.jDiffer.shapes.Ray;
import uk.aidanlee.jDiffer.shapes.Shape;

import java.util.LinkedList;
import java.util.List;

public class Collision {
    public static ShapeCollision shapeWithShape(Shape _shape1, Shape _shape2)
    {
        return _shape1.test(_shape2);
    }

    public static List<ShapeCollision> shapeWithShapes(Shape _shape, Shape[] _shapes) {
        List<ShapeCollision> results = new LinkedList<>();

        for (Shape otherShape : _shapes) {
            ShapeCollision result = shapeWithShape(_shape, otherShape);
            if (result != null) {
                results.add(result);
            }
        }

        return results;
    }

    public static RayCollision rayWithShape(Ray _ray, Shape _shape) {
        return _shape.testRay(_ray);
    }
    public static List<RayCollision> rayWithShapes(Ray _ray, Shape[] _shapes) {
        List<RayCollision> results = new LinkedList<>();

        for (Shape otherShape : _shapes) {
            RayCollision result = rayWithShape(_ray, otherShape);
            if (result != null) {
                results.add(result);
            }
        }

        return results;
    }

    public static RayIntersection rayWithRay(Ray _ray1, Ray _ray2) {
        return SAT2D.testRayVsRay(_ray1, _ray2);
    }

    public static List<RayIntersection> rayWithRays(Ray _ray, Ray[] _rays) {
        List<RayIntersection> results = new LinkedList<>();

        for (Ray otherRay : _rays) {
            RayIntersection result = rayWithRay(_ray, otherRay);
            if (result != null) {
                results.add(result);
            }
        }

        return results;
    }

    public static boolean pointInPoly(double _x, double _y, Polygon _poly) {
        Vector[] verts = _poly.getTransformedVertices();
        int sides = verts.length;

        boolean oddNodes = false;
        int j = sides - 1;

        for (int i = 0; i < sides; i++) {
            if( (verts[i].y < _y && verts[j].y >= _y) ||
                    (verts[j].y < _y && verts[i].y >= _y))
            {
                if( verts[i].x +
                    (_y - verts[i].y) /
                        (verts[j].y - verts[i].y) *
                        (verts[j].x - verts[i].x) < _x)
                {
                    oddNodes = !oddNodes;
                }

            }

            j = i;
        }

        return oddNodes;
    }
}
