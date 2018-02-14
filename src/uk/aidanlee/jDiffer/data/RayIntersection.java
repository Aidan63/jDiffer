package uk.aidanlee.jDiffer.data;

import uk.aidanlee.jDiffer.shapes.Ray;

public class RayIntersection {
    public Ray ray1;
    public Ray ray2;

    public double u1 = 0;
    public double u2 = 0;

    public RayIntersection reset() {
        ray1 = null;
        ray2 = null;
        u1 = 0;
        u2 = 0;

        return this;
    }

    public void copyFrom(RayIntersection _other) {
        ray1 = _other.ray1;
        ray2 = _other.ray2;
        u1 = _other.u1;
        u2 = _other.u2;
    }

    public RayIntersection clone() {
        RayIntersection clone = new RayIntersection();
        clone.copyFrom(this);

        return clone;
    }
}
