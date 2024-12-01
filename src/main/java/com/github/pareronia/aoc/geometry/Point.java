package com.github.pareronia.aoc.geometry;

import java.util.Objects;

public class Point {
    private final Integer x;
    private final Integer y;
    
    protected Point(final Integer x, final Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }
    
    public Point withX(final Integer x) {
        return new Point(x, this.y);
    }
    
    public Point withY(final Integer y) {
        return new Point(this.x, y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
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
        final Point other = (Point) obj;
        return Objects.equals(x, other.x) && Objects.equals(y, other.y);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Point [x=").append(x).append(", y=").append(y).append("]");
        return builder.toString();
    }
}
