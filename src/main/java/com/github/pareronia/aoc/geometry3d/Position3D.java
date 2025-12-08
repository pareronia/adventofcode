package com.github.pareronia.aoc.geometry3d;

import java.util.stream.Stream;

public class Position3D extends Point3D {

    public Position3D(final int x, final int y, final int z) {
        super(x, y, z);
    }

    public static Position3D of(final int x, final int y, final int z) {
        return new Position3D(x, y, z);
    }

    public Position3D translate(final Vector3D vector) {
        return translate(vector, 1);
    }

    public Position3D translate(final Vector3D vector, final int amplitude) {
        return new Position3D(
                this.getX() + amplitude * vector.getX(),
                this.getY() + amplitude * vector.getY(),
                this.getZ() + amplitude * vector.getZ());
    }

    public double distance(final Position3D other) {
        return Math.sqrt(this.squaredDistance(other));
    }

    public long squaredDistance(final Position3D other) {
        final long dx = other.getX() - this.getX();
        final long dy = other.getY() - this.getY();
        final long dz = other.getZ() - this.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    public Integer manhattanDistance(final Position3D from) {
        return Math.abs(this.getX() - from.getX())
                + Math.abs(this.getY() - from.getY())
                + Math.abs(this.getZ() - from.getZ());
    }

    public Stream<Position3D> capitalNeighbours() {
        return Direction3D.CAPITAL.stream().map(Direction3D::getValue).map(this::translate);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(50);
        builder.append("Position3D(x=")
                .append(getX())
                .append(", y=")
                .append(getY())
                .append(", z=")
                .append(getZ())
                .append(')');
        return builder.toString();
    }
}
