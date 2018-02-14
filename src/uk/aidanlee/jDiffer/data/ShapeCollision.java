package uk.aidanlee.jDiffer.data;

import uk.aidanlee.jDiffer.shapes.Shape;

public class ShapeCollision {
    public double overlap;
    public double separationX;
    public double separationY;
    public double unitVectorX;
    public double unitVectorY;

    public double otherOverlap;
    public double otherSeparationX;
    public double otherSeparationY;
    public double otherUnitVectorX;
    public double otherUnitVectorY;

    public Shape shape1;
    public Shape shape2;

    public ShapeCollision reset() {
        shape1 = shape2 = null;
        overlap = separationX = separationY = unitVectorX = unitVectorY = 0;
        otherOverlap = otherSeparationX = otherSeparationY = otherUnitVectorX = otherUnitVectorY;

        return this;
    }

    public ShapeCollision clone() {
        ShapeCollision clone = new ShapeCollision();
        clone.copyFrom(this);

        return clone;
    }

    public void copyFrom(ShapeCollision _other) {
        overlap = _other.overlap;

        separationX = _other.separationX;
        separationY = _other.separationY;
        unitVectorX = _other.unitVectorX;
        unitVectorY = _other.unitVectorY;

        otherOverlap = _other.overlap;
        otherSeparationX = _other.otherSeparationX;
        otherSeparationY = _other.otherSeparationY;
        otherUnitVectorX = _other.otherUnitVectorX;
        otherUnitVectorY = _other.otherUnitVectorY;

        shape1 = _other.shape1;
        shape2 = _other.shape2;
    }
}
