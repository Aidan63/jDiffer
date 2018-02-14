package uk.aidanlee.jDiffer.data;

import uk.aidanlee.jDiffer.shapes.Ray;
import uk.aidanlee.jDiffer.shapes.Shape;

public class RayCollision {
    public Shape shape;
    public Ray   ray;

    public double start;
    public double end;

    public RayCollision reset() {
        ray   = null;
        shape = null;
        start = 0;
        end   = 0;

        return this;
    }

    public void copyFrom(RayCollision _other) {
        ray   = _other.ray;
        shape = _other.shape;
        start = _other.start;
        end   = _other.end;
    }

    public RayCollision clone() {
        RayCollision clone = new RayCollision();
        clone.copyFrom(this);

        return clone;
    }

    public double hitStartX(RayCollision _data) {
        return _data.ray.start.x + (_data.ray.getDir().x * _data.start);
    }
    public double hitStartY(RayCollision _data) {
        return _data.ray.start.y + (_data.ray.getDir().y * _data.start);
    }
    public double hitEndX(RayCollision _data) {
        return _data.ray.start.x + (_data.ray.getDir().x * _data.end);
    }
    public double hitEndY(RayCollision _data) {
        return _data.ray.start.x + (_data.ray.getDir().x * _data.end);
    }
}
