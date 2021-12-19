package com.github.pareronia.aoc.geometry3d;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Transformations3D {
    
    public static List<Position3D> translate(final List<Position3D> positions, final Vector3D vector) {
        return positions.stream()
                .map(p -> translate(p, vector))
                .collect(toList());
    }
    
    // https://www.euclideanspace.com/maths/algebra/matrix/transforms/examples/index.htm
    public static final List<double[][]> ALL_TRANSFORMATIONS = List.of(
        new double[][] {
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, -1d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, -1d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { -1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        },
        new double[][] {
            new double[] { 0d, 1d, 0d, 0d },
            new double[] { 0d, 0d, 1d, 0d },
            new double[] { 1d, 0d, 0d, 0d },
            new double[] { 0d, 0d, 0d, 1d }
        }
    );
    
    public static List<Position3D> apply(final List<Position3D> positions, final double[][] matrix) {
        final RealMatrix rm = MatrixUtils.createRealMatrix(matrix);
        return positions.stream()
                .map(Transformations3D::asMatrix)
                .map(rm::multiply)
                .map(Transformations3D::fromMatrix)
                .collect(toList());
    }
    
    public static Position3D translate(final Position3D position, final Vector3D vector) {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            new double[] { 1d, 0d, 0d, vector.getX() },
            new double[] { 0d, 1d, 0d, vector.getY() },
            new double[] { 0d, 0d, 1d, vector.getZ() },
            new double[] { 0d, 0d, 0d, 1d }
        });
        return fromMatrix(matrix.multiply(asMatrix(position)));
    }
    
    private static Position3D fromMatrix(final RealMatrix product) {
        return Position3D.of(
                (int) product.getColumn(0)[0],
                (int) product.getColumn(0)[1],
                (int) product.getColumn(0)[2]);
    }

    private static RealMatrix asMatrix(final Position3D position) {
        return MatrixUtils.createRealMatrix(new double[][] {
            new double[] { position.getX() },
            new double[] { position.getY() },
            new double[] { position.getZ() },
            new double[] { 1d }
        });
    }
}
