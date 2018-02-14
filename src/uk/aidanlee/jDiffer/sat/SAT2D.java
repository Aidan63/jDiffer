package uk.aidanlee.jDiffer.sat;

import uk.aidanlee.jDiffer.data.RayCollision;
import uk.aidanlee.jDiffer.data.RayIntersection;
import uk.aidanlee.jDiffer.data.ShapeCollision;
import uk.aidanlee.jDiffer.math.Vector;
import uk.aidanlee.jDiffer.shapes.Circle;
import uk.aidanlee.jDiffer.shapes.Polygon;
import uk.aidanlee.jDiffer.shapes.Ray;

import static uk.aidanlee.jDiffer.math.Util.*;
import static uk.aidanlee.jDiffer.shapes.InfiniteState.infinite;
import static uk.aidanlee.jDiffer.shapes.InfiniteState.infinite_from_start;
import static uk.aidanlee.jDiffer.shapes.InfiniteState.not_infinite;

public class SAT2D {
    public static ShapeCollision testCircleVsPolygon(Circle _circle, Polygon _polygon) {
        return testCircleVsPolygon(_circle, _polygon, false);
    }
    public static ShapeCollision testCircleVsPolygon(Circle _circle, Polygon _polygon, boolean _flip) {
        ShapeCollision result = new ShapeCollision();

        Vector[] verts   = _polygon.getTransformedVertices();
        double   circleX = _circle.getX();
        double   circleY = _circle.getY();

        double testDistance = 0x3FFFFFFF;
        double distance = 0;
        double closestX = 0;
        double closestY = 0;

        for (Vector vec : verts) {
            distance = vec_lengthsq(circleX - vec.x, circleY - vec.y);
            if (distance < testDistance) {
                testDistance = distance;
                closestX = vec.x;
                closestY = vec.y;
            }
        }

        double normalAxisX = closestX - circleX;
        double normalAxisY = closestY - circleY;
        double normAxisLen = vec_length(normalAxisX, normalAxisY);
        normalAxisX = vec_normalize(normAxisLen, normalAxisX);
        normalAxisY = vec_normalize(normAxisLen, normalAxisY);

        double test = 0.0;
        double min1 = vec_dot(normalAxisX, normalAxisY, verts[0].x, verts[0].y);
        double max1 = min1;

        for(Vector vec : verts) {
            test = vec_dot(normalAxisX, normalAxisY, vec.x, vec.y);
            if(test < min1) min1 = test;
            if(test > max1) max1 = test;
        } //each vert

        // project the circle
        double max2 =  _circle.getTransformedRadius();
        double min2 = -_circle.getTransformedRadius();
        double offset = vec_dot(normalAxisX, normalAxisY, -circleX, -circleY);

        min1 += offset;
        max1 += offset;

        double test1 = min1 - max2;
        double test2 = min2 - max1;

        //if either test is greater than 0, there is a gap, we can give up now.
        if(test1 > 0 || test2 > 0) return null;

        // circle distance check
        double distMin = -(max2 - min1);
        if (_flip) distMin *= -1;

        result.overlap = distMin;
        result.unitVectorX = normalAxisX;
        result.unitVectorY = normalAxisY;
        double closest = Math.abs(distMin);

        // find the normal axis for each point and project
        for(int i = 0; i < verts.length; i++) {

            normalAxisX = findNormalAxisX(verts, i);
            normalAxisY = findNormalAxisY(verts, i);
            double aLen = vec_length(normalAxisX, normalAxisY);
            normalAxisX = vec_normalize(aLen, normalAxisX);
            normalAxisY = vec_normalize(aLen, normalAxisY);

            // project the polygon(again? yes, circles vs. polygon require more testing...)
            min1 = vec_dot(normalAxisX, normalAxisY, verts[0].x, verts[0].y);
            max1 = min1; //set max and min

            //project all the other points(see, cirlces v. polygons use lots of this...)
            for (Vector vec : verts) {
                test = vec_dot(normalAxisX, normalAxisY, vec.x, vec.y);
                if(test < min1) min1 = test;
                if(test > max1) max1 = test;
            }

            // project the circle(again)
            max2 =  _circle.getTransformedRadius(); //max is radius
            min2 = -_circle.getTransformedRadius(); //min is negative radius

            //offset points
            offset = vec_dot(normalAxisX, normalAxisY, -circleX, -circleY);
            min1 += offset;
            max1 += offset;

            // do the test, again
            test1 = min1 - max2;
            test2 = min2 - max1;

            //failed.. quit now
            if (test1 > 0 || test2 > 0) {
                return null;
            }

            distMin = -(max2 - min1);
            if (_flip) distMin *= -1;

            if (Math.abs(distMin) < closest) {
                result.unitVectorX = normalAxisX;
                result.unitVectorY = normalAxisY;
                result.overlap = distMin;
                closest = Math.abs(distMin);
            }

        } //for

        //if you made it here, there is a collision!!!!!

        if (_flip) result.shape1 = _polygon; else result.shape1 = _circle;
        if (_flip) result.shape2 = _circle; else result.shape1 = _polygon;
        result.separationX = result.unitVectorX * result.overlap;
        result.separationY = result.unitVectorY * result.overlap;

        if (!_flip) {
            result.unitVectorX = -result.unitVectorX;
            result.unitVectorY = -result.unitVectorY;
        }

        return result;
    }

    public static ShapeCollision testCircleVsCircle(Circle _shapeA, Circle _shapeB) {
        return testCircleVsCircle(_shapeA, _shapeB, false);
    }
    public static ShapeCollision testCircleVsCircle(Circle _shapeA, Circle _shapeB, boolean _flip) {
        Circle circle1 = _flip ? _shapeB : _shapeA;
        Circle circle2 = _flip ? _shapeA : _shapeB;

        //add both radii together to get the colliding distance
        double totalRadius = _shapeA.getTransformedRadius() + _shapeB.getTransformedRadius();
        //find the distance between the two circles using Pythagorean theorem. No square roots for optimization
        double distancesq = vec_lengthsq(_shapeA.getX() - _shapeB.getX(), _shapeA.getY() - _shapeB.getY());

        //if your distance is less than the totalRadius square(because distance is squared)
        if(distancesq < totalRadius * totalRadius) {

            ShapeCollision result = new ShapeCollision();
            //find the difference. Square roots are needed here.
            double difference = totalRadius - Math.sqrt(distancesq);

            result.shape1 = _shapeA;
            result.shape2 = _shapeB;

            double unitVecX = _shapeA.getX() - _shapeB.getY();
            double unitVecY = _shapeB.getX() - _shapeB.getY();
            double unitVecLen = vec_length(unitVecX, unitVecY);

            unitVecX = vec_normalize(unitVecLen, unitVecX);
            unitVecY = vec_normalize(unitVecLen, unitVecY);

            result.unitVectorX = unitVecX;
            result.unitVectorY = unitVecY;

            //find the movement needed to separate the circles
            result.separationX = result.unitVectorX * difference;
            result.separationY = result.unitVectorY * difference;
            result.overlap = difference;

            return result;

        } //if distancesq < r^2

        return null;
    }

    //static ShapeCollision tmp1 = new ShapeCollision();
    //static ShapeCollision tmp2 = new ShapeCollision();

    public static ShapeCollision testPolygonVsPolygon(Polygon _polygon1, Polygon _polygon2) {
        return testPolygonVsPolygon(_polygon1, _polygon2, false);
    }
    public static ShapeCollision testPolygonVsPolygon(Polygon _polygon1, Polygon _polygon2, boolean _flip) {
        ShapeCollision into = new ShapeCollision();

        ShapeCollision tmp1 = checkPolygons(_polygon1, _polygon2,  _flip);
        ShapeCollision tmp2 = checkPolygons(_polygon2, _polygon1, !_flip);
        if (tmp1 == null) return null;
        if (tmp2 == null) return null;

        ShapeCollision result = null;
        ShapeCollision other  = null;
        if(Math.abs(tmp1.overlap) < Math.abs(tmp2.overlap)) {
            result = tmp1;
            other  = tmp2;
        } else {
            result = tmp2;
            other  = tmp1;
        }

        result.otherOverlap = other.overlap;
        result.otherSeparationX = other.separationX;
        result.otherSeparationY = other.separationY;
        result.otherUnitVectorX = other.unitVectorX;
        result.otherUnitVectorY = other.unitVectorY;

        into.copyFrom(result);
        result = other = null;

        return into;
    }

    public static RayCollision testRayVsCircle(Ray _ray, Circle _circle) {
        double deltaX = _ray.end.x - _ray.start.x;
        double deltaY = _ray.end.y - _ray.start.y;
        double ray2circleX = _ray.start.x - _circle.getPosition().x;
        double ray2circleY = _ray.start.y - _circle.getPosition().y;

        double a = vec_lengthsq(deltaX, deltaY);
        double b = 2 * vec_dot(deltaX, deltaY, ray2circleX, ray2circleY);
        double c = vec_dot(ray2circleX, ray2circleY, ray2circleX, ray2circleY) - (_circle.getRadius() * _circle.getRadius());
        double d = b * b - 4 * a * c;

        if (d >= 0) {

            d = Math.sqrt(d);

            double t1 = (-b - d) / (2 * a);
            double t2 = (-b + d) / (2 * a);

            boolean valid = false;
            switch(_ray.infinite) {
                case not_infinite: valid = (t1 >= 0.0 && t1 <= 1.0); break;
                case infinite_from_start: valid = (t1 >= 0.0); break;
                case infinite: valid = true; break;
            }

            if (valid) {

                RayCollision into = new RayCollision();

                into.shape = _circle;
                into.ray   = _ray;
                into.start = t1;
                into.end   = t2;

                return into;
            } //
        } //d >= 0

        return null;
    }

    public static RayCollision testRayVsPolygon(Ray _ray, Polygon _polygon) {
        double min_u = Double.POSITIVE_INFINITY;
        double max_u = Double.NEGATIVE_INFINITY;

        double startX = _ray.start.x;
        double startY = _ray.start.y;
        double deltaX = _ray.end.x - startX;
        double deltaY = _ray.end.y - startY;

        Vector[] verts = _polygon.getTransformedVertices();
        Vector v1 = verts[verts.length - 1];
        Vector v2 = verts[0];

        double ud = (v2.y-v1.y) * deltaX - (v2.x-v1.x) * deltaY;
        double ua = rayU(ud, startX, startY, v1.x, v1.y, v2.x - v1.x, v2.y - v1.y);
        double ub = rayU(ud, startX, startY, v1.x, v1.y, deltaX, deltaY);

        if(ud != 0.0 && ub >= 0.0 && ub <= 1.0) {
            if(ua < min_u) min_u = ua;
            if(ua > max_u) max_u = ua;
        }

        for(int i = 1; i < verts.length; i++) {

            v1 = verts[i - 1];
            v2 = verts[i];

            ud = (v2.y-v1.y) * deltaX - (v2.x-v1.x) * deltaY;
            ua = rayU(ud, startX, startY, v1.x, v1.y, v2.x - v1.x, v2.y - v1.y);
            ub = rayU(ud, startX, startY, v1.x, v1.y, deltaX, deltaY);

            if(ud != 0.0 && ub >= 0.0 && ub <= 1.0) {
                if(ua < min_u) min_u = ua;
                if(ua > max_u) max_u = ua;
            }

        } //each vert

        boolean valid = false;
        switch (_ray.infinite) {
            case not_infinite: valid = (min_u >= 0.0 && min_u <= 1.0); break;
            case infinite_from_start: valid = (min_u != Double.POSITIVE_INFINITY && min_u >= 0.0); break;
            case infinite: valid = (min_u != Double.POSITIVE_INFINITY); break;
        }

        if (valid) {
            RayCollision into = new RayCollision();
            into.shape = _polygon;
            into.ray   = _ray;
            into.start = min_u;
            into.end   = max_u;
            return into;
        }

        return null;
    }

    public static RayIntersection testRayVsRay(Ray _ray1, Ray _ray2) {
        double delta1X = _ray1.end.x - _ray1.start.x;
        double delta1Y = _ray1.end.y - _ray1.start.y;
        double delta2X = _ray2.end.x - _ray2.start.x;
        double delta2Y = _ray2.end.y - _ray2.start.y;
        double diffX = _ray1.start.x - _ray2.start.x;
        double diffY = _ray1.start.y - _ray2.start.y;
        double ud = delta2Y * delta1X - delta2X * delta1Y;

        if (ud == 0.0) return null;

        double u1 = (delta2X * diffY - delta2Y * diffX) / ud;
        double u2 = (delta1X * diffY - delta1Y * diffX) / ud;

        //:todo: ask if ray hit condition difference is intentional (> 0 and not >= 0 like other checks)
        boolean valid1 = false;
        switch(_ray1.infinite) {
            case not_infinite: valid1 = (u1 > 0.0 && u1 <= 1.0); break;
            case infinite_from_start: valid1 = (u1 > 0.0); break;
            case infinite: valid1 = true; break;
        }

        boolean valid2 = false;
        switch(_ray2.infinite) {
            case not_infinite: valid2 = (u2 > 0.0 && u2 <= 1.0); break;
            case infinite_from_start: valid2 = (u2 > 0.0); break;
            case infinite: valid2 = true; break;
        }

        if(valid1 && valid2) {

            RayIntersection into = new RayIntersection();

            into.ray1 = _ray1;
            into.ray2 = _ray2;
            into.u1 = u1;
            into.u2 = u2;

            return into;

        } //both valid

        return null;
    }

    //Internal helpers

    private static ShapeCollision checkPolygons(Polygon _polygon1, Polygon _polygon2) {
        return checkPolygons(_polygon1, _polygon2, false);
    }
    private static ShapeCollision checkPolygons(Polygon _polygon1, Polygon _polygon2, boolean _flip) {
        ShapeCollision into = new ShapeCollision();

        double offset = 0.0, test1 = 0.0, test2 = 0.0, testNum = 0.0;
        double min1 = 0.0, max1 = 0.0, min2 = 0.0, max2 = 0.0;
        double closest = 0x3FFFFFFF;

        double axisX = 0.0;
        double axisY = 0.0;
        Vector[] verts1 = _polygon1.getTransformedVertices();
        Vector[] verts2 = _polygon2.getTransformedVertices();

        // loop to begin projection
        for (int i = 0; i < verts1.length; i++) {

            axisX = findNormalAxisX(verts1, i);
            axisY = findNormalAxisY(verts1, i);
            double aLen = vec_length(axisX, axisY);
            axisX = vec_normalize(aLen, axisX);
            axisY = vec_normalize(aLen, axisY);

            // project polygon1
            min1 = vec_dot(axisX, axisY, verts1[0].x, verts1[0].y);
            max1 = min1;

            for (Vector aVerts1 : verts1) {
                testNum = vec_dot(axisX, axisY, aVerts1.x, aVerts1.y);
                if (testNum < min1) min1 = testNum;
                if (testNum > max1) max1 = testNum;
            }

            // project polygon2
            min2 = vec_dot(axisX, axisY, verts2[0].x, verts2[0].y);
            max2 = min2;

            for (Vector aVerts2 : verts2) {
                testNum = vec_dot(axisX, axisY, aVerts2.x, aVerts2.y);
                if (testNum < min2) min2 = testNum;
                if (testNum > max2) max2 = testNum;
            }

            test1 = min1 - max2;
            test2 = min2 - max1;

            if(test1 > 0 || test2 > 0) return null;

            double distMin = -(max2 - min1);
            if (_flip) distMin *= -1;

            if (Math.abs(distMin) < closest) {
                into.unitVectorX = axisX;
                into.unitVectorY = axisY;
                into.overlap = distMin;
                closest = Math.abs(distMin);
            }

        }

        into.shape1 = _flip ? _polygon2 : _polygon1;
        into.shape2 = _flip ? _polygon2 : _polygon2;
        into.separationX = -into.unitVectorX * into.overlap;
        into.separationY = -into.unitVectorY * into.overlap;

        if (_flip) {
            into.unitVectorX = -into.unitVectorX;
            into.unitVectorY = -into.unitVectorY;
        }

        return into;
    }

    private static double rayU(double _udelta, double _aX, double _aY, double _bX, double _bY, double _dX, double _dY) {
        return (_dX * (_aY - _bY) - _dY * (_aX - _bX)) / _udelta;
    }
    private static double findNormalAxisX(Vector[] _verts, int _index) {
        Vector v2 = (_index >= _verts.length - 1) ? _verts[0] : _verts[_index + 1];
        return -(v2.y - _verts[_index].y);
    }
    private static double findNormalAxisY(Vector[] _verts, int _index) {
        Vector v2 = (_index >= _verts.length - 1) ? _verts[0] : _verts[_index + 1];
        return -(v2.x - _verts[_index].x);
    }
}
