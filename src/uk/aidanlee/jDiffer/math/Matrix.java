package uk.aidanlee.jDiffer.math;

public class Matrix {
    public double a;
    public double b;
    public double c;
    public double d;
    public double tx;
    public double ty;

    private double lastRotation = 0;

    public Matrix() {
        a = 1;
        b = 0;
        c = 0;
        d = 1;
        tx = 0;
        ty = 0;
    }

    public Matrix(double _a, double _b, double _c, double _d, double _tx, double _ty) {
        a = _a;
        b = _b;
        c = _c;
        d = _d;
        tx = _tx;
        ty = _ty;
    }

    public void identity() {
        a = 1;
        b = 0;
        c = 0;
        d = 1;
        tx = 0;
        ty = 0;
    }

    public void translate(double _x, double _y) {
        tx += _x;
        ty += _y;
    }

    public void compose(Vector _position, double _rotation, Vector _scale) {
        identity();

        scale(_scale.x, _scale.y);
        rotate(_rotation);
        makeTranslation(_position.x, _position.y);
    }

    public Matrix makeTranslation(double _x, double _y) {
        tx = _x;
        ty = _y;

        return this;
    }

    public void rotate(double _angle) {
        double cos = Math.cos(_angle);
        double sin = Math.sin(_angle);

        double a1 = a * cos - b * sin;
        b = a * sin + b * cos;
        a = a1;

        double c1 = c * cos - d * sin;
        d = c * sin + d * cos;
        c = c1;

        double tx1 = tx * cos - ty * sin;
        ty = tx * sin + ty * cos;
        tx = tx1;
    }

    public void scale(double _x, double _y) {
        a *= _x;
        b *= _y;

        c *= _x;
        d *= _y;

        tx *= _x;
        ty *= _y;
    }

    @Override
    public String toString() {
        return "(a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", tx=" + tx + ", ty=" + ty + ")";
    }
}
