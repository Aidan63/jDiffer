package uk.aidanlee.jDiffer.math;

public class Util {
    public static double vec_lengthsq(double _x, double _y) {
        return _x * _x + _y * _y;
    }
    public static double vec_length(double _x, double _y) {
        return Math.sqrt(vec_lengthsq(_x, _y));
    }
    public static double vec_normalize(double _length, double _component) {
        if (_length == 0) return 0;
        return _component /= _length;
    }
    public static double vec_dot(double _x, double _y, double _otherX, double _otherY) {
        return _x * _otherX + _y * _otherY;
    }
}
