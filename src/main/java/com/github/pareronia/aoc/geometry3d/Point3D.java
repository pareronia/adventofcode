package com.github.pareronia.aoc.geometry3d;

import java.util.Objects;

public class Point3D {
    private final int x;
    private final int y;
    private final int z;
    
    protected Point3D(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
    public Point3D withX(final int x) {
        return new Point3D(x, this.y, this.z);
    }
    
    public Point3D withY(final int y) {
        return new Point3D(this.x, y, this.z);
    }
    
    public Point3D withZ(final int z) {
        return new Point3D(this.x, this.y, z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point3D other = (Point3D) obj;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Point3D [x=").append(x).append(", y=").append(y).append(", z=").append(z).append("]");
        return builder.toString();
    }
}
