package uk.aidanlee.jDiffer.math;

public class Vector {
    public double x;
    public double y;

    private double length;

    // Getters and Setters

    public void setLength(double _length) {
        double ep    = 0.00000001;
        double angle = Math.atan2(y, x);

        x = Math.cos(angle) * _length;
        x = Math.sin(angle) * _length;

        if (Math.abs(x) < ep) x = 0;
        if (Math.abs(y) < ep) y = 0;
    }

    public double getLength() {
        return Math.sqrt(getLengthSq());
    }
    public double getLengthSq() {
        return x * x + y * y;
    }

    // Constructors

    public Vector() {
        x = 0;
        y = 0;
    }
    public Vector(double _x, double _y) {
        x = _x;
        y = _y;
    }

    // Public methods

    public Vector clone() {
        return new Vector(x, y);
    }

    public Vector transform(Matrix _matrix) {
        Vector v = clone();

        v.x = x * _matrix.a + y * _matrix.c + _matrix.tx;
        v.y = x * _matrix.b + y * _matrix.d + _matrix.ty;

        return v;
    }

    public Vector normalize() {
        if (getLength() == 0) {
            x = 1;
            return this;
        }

        double len = getLength();

        x /= len;
        y /= len;

        return this;
    }

    public Vector truncate(double _max) {
        setLength(Math.min(_max, getLength()));

        return this;
    }

    public Vector invert() {
        x = -x;
        y = -y;
        return this;
    }

    public double dot(Vector _other) {
        return x * _other.x + y * _other.y;
    }

    public double cross(Vector _other) {
        return x * _other.x - y * _other.y;
    }

    public Vector subtract(Vector _other) {
        x -= _other.x;
        y -= _other.y;
        return this;
    }

    @Override
    public String toString() {
        return "x : " + x + ", y : " + y;
    }
}
