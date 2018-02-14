package uk.aidanlee.jDiffer.shapes;

import uk.aidanlee.jDiffer.data.RayCollision;
import uk.aidanlee.jDiffer.data.ShapeCollision;
import uk.aidanlee.jDiffer.math.Matrix;
import uk.aidanlee.jDiffer.math.Vector;

import java.util.HashMap;
import java.util.Map;

public class Shape {
    public boolean active = true;
    public String name = "shape";
    public Object data;
    public Map<String, String> tags;

    protected Vector position;
    protected double rotation;
    protected double scaleX;
    protected double scaleY;
    protected Vector scale;

    private Vector _position;
    private double _rotation;
    private double _rotation_radians;

    protected double _scaleX;
    protected double _scaleY;

    protected boolean transformed = false;
    protected Matrix  transformMatrix;

    public Shape(double _x, double _y) {
        tags = new HashMap<>();

        position = new Vector(_x, _y);
        scale    = new Vector(1, 1);
        rotation = 0;

        scaleX = 1;
        scaleY = 1;

        transformMatrix = new Matrix();
        transformMatrix.makeTranslation(position.x, position.y);
    }

    // Public API

    public ShapeCollision test(Shape _shape) {
        return null;
    }

    public ShapeCollision testCircle(Circle _circle, boolean _flip) {
        return null;
    }

    public ShapeCollision testPolygon(Polygon _polygon, boolean _flip) {
        return null;
    }

    public RayCollision testRay(Ray _ray) {
        return null;
    }

    public void destroy() {
        position = null;
        scale    = null;
        transformMatrix = null;
    }

    // Getters and Setters

    private void refreshTransformation() {
        transformMatrix.compose(position, _rotation_radians, scale);
        transformed = false;
    }

    public Vector getPosition() {
        return position;
    }
    public void setPosition(Vector _position) {
        this.position = _position;
        refreshTransformation();
    }

    public double getX() {
        return position.x;
    }
    public void setX(double _x) {
        position.x = _x;
        refreshTransformation();
    }

    public double getY() {
        return position.y;
    }
    public void setY(double _y) {
        position.y = _y;
        refreshTransformation();
    }

    public double getRotation() {
        return rotation;
    }
    public void setRotation(double _v) {
        _rotation_radians = _v * (Math.PI / 180);
    }

    public double getScaleX() {
        return scaleX;
    }
    public void setScaleX(double _scaleX) {
        scaleX  = _scaleX;
        scale.x = _scaleX;
        refreshTransformation();
    }

    public double getScaleY() {
        return scaleY;
    }
    public void setScaleY(double _scaleY) {
        scaleY  = _scaleY;
        scale.y = _scaleY;
        refreshTransformation();
    }
}
