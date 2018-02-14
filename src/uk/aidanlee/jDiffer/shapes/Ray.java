package uk.aidanlee.jDiffer.shapes;

import uk.aidanlee.jDiffer.math.Vector;



public class Ray {
    public Vector start;
    public Vector end;
    public InfiniteState infinite;

    private Vector dirCache;

    public Ray(Vector _start, Vector _end) {
        start    = _start;
        end      = _end;
        infinite = InfiniteState.not_infinite;

        dirCache = new Vector(end.x - start.x, end.y - start.y);
    }

    public Ray(Vector _start, Vector _end, InfiniteState _infinite) {
        start    = _start;
        end      = _end;
        infinite = _infinite;

        dirCache = new Vector(end.x - start.x, end.y - start.y);
    }

    public Vector getDir() {
        dirCache.x = end.x - start.x;
        dirCache.y = end.y - start.y;
        return dirCache;
    }
}
