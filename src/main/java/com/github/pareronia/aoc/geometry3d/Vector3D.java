package com.github.pareronia.aoc.geometry3d;

public class Vector3D extends Point3D {

    public Vector3D(final int x, final int y, final int z) {
        super(x, y, z);
    }
    
    public static Vector3D of(final int x, final int y, final int z) {
        return new Vector3D(x, y, z);
    }
    
    public static Vector3D from(final Point3D point) {
        return new Vector3D(point.getX(), point.getY(), point.getZ());
    }
    
    public static Vector3D from(final Position3D from, final Position3D to) {
        return new Vector3D(
                to.getX() - from.getX(),
                to.getY() - from.getY(),
                to.getZ() - from.getZ());
    }

    public Vector3D add(final Vector3D vector, final Integer amplitude) {
        final Point3D point = this
                .withX(this.getX() + vector.getX() * amplitude)
                .withY(this.getY() + vector.getY() * amplitude)
                .withZ(this.getZ() + vector.getZ() * amplitude);
        return Vector3D.from(point);
    }
}
