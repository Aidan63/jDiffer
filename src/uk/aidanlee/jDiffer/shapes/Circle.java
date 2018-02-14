package uk.aidanlee.jDiffer.shapes;

import uk.aidanlee.jDiffer.data.RayCollision;
import uk.aidanlee.jDiffer.data.ShapeCollision;
import uk.aidanlee.jDiffer.sat.SAT2D;

public class Circle extends Shape {
    private double radius;

    public Circle(double _x, double _y, double _radius) {
        super(_x, _y);

        radius = _radius;
        name   = "circle " + radius;
    }

    @Override
    public ShapeCollision test(Shape _shape) {
        return _shape.testCircle(this, true);
    }

    @Override
    public ShapeCollision testCircle(Circle _circle, boolean _flip) {
        return SAT2D.testCircleVsCircle(this, _circle, _flip);
    }

    @Override
    public ShapeCollision testPolygon(Polygon _polygon, boolean _flip) {
        return SAT2D.testCircleVsPolygon(this, _polygon, _flip);
    }

    @Override
    public RayCollision testRay(Ray _ray) {
        return SAT2D.testRayVsCircle(_ray, this);
    }

    public double getRadius() {
        return radius;
    }

    public double getTransformedRadius() {
        return radius * scaleX;
    }
}
