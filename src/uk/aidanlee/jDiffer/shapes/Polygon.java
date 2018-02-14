package uk.aidanlee.jDiffer.shapes;

import uk.aidanlee.jDiffer.data.RayCollision;
import uk.aidanlee.jDiffer.data.ShapeCollision;
import uk.aidanlee.jDiffer.math.Vector;
import uk.aidanlee.jDiffer.sat.SAT2D;

public class Polygon extends Shape {
    private Vector[] transformedVertices;
    private Vector[] vertices;

    public Polygon(double _x, double _y, Vector[] _vertices) {
        super(_x, _y);

        name = "polygon(sides:" + _vertices.length + ")";

        transformedVertices = new Vector[_vertices.length];
        vertices = _vertices;
    }

    @Override
    public ShapeCollision test(Shape _shape) {
        return _shape.testPolygon(this, true);
    }

    @Override
    public ShapeCollision testCircle(Circle _circle, boolean _flip) {
        return SAT2D.testCircleVsPolygon(_circle, this, !_flip);
    }

    @Override
    public ShapeCollision testPolygon(Polygon _polygon, boolean _flip) {
        return SAT2D.testPolygonVsPolygon(this, _polygon, _flip);
    }

    @Override
    public RayCollision testRay(Ray _ray) {
        return SAT2D.testRayVsPolygon(_ray, this);
    }

    public Vector[] getTransformedVertices() {
        if (!transformed) {
            transformedVertices = new Vector[vertices.length];
            transformed = true;

            for (int i = 0; i < vertices.length; i++) {
                transformedVertices[i] = vertices[i].clone().transform(transformMatrix);
            }
        }

        return transformedVertices;
    }

    public Vector[] getVertices() {
        return vertices;
    }
}
