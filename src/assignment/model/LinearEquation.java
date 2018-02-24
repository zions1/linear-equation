package assignment.model;

import assignment.exception.LinearEquationException;
import assignment.exception.MatrixException;
import assignment.exception.VectorException;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

/**
 * Calculates the LU decomposition of a matrix.
 * <br>Solves the linear equation A × X = B for matrices A and vector B.
 *
 * @author Wiktor Florencki
 * @version 1.0
 * @see <a href="http://mathworld.wolfram.com/LUDecomposition.html">LU Decomposition</a>
 */
public class LinearEquation {

    /**
     * The matrix.
     *
     * @see RealMatrix
     */
    private RealMatrix matrix;

    /**
     * The vector.
     *
     * @see RealVector
     */
    private RealVector vector;

    /**
     * The LU-decomposition.
     *
     * @see LUDecomposition
     */
    private LUDecomposition luDecomposition;

    /**
     * Calculates the LU-decomposition of the given matrix.
     *
     * @param matrixString the input matrix in {@code String} format.
     * @param vectorString the input matrix in {@code String} format.
     * @throws LinearEquationException if the errors during the conversion or LU-decomposition process appeared.
     */
    public LinearEquation(String matrixString, String vectorString) throws LinearEquationException {
        createRealMatrix(convertStringToDouble2DArray(matrixString));
        createRealVector(convertStringToDoubleArray(vectorString));
        luDecomposition = new LUDecomposition(matrix);
    }

    /**
     * Calculates the LU-decomposition of the given matrix.
     *
     * @param matrixString the input matrix in {@code String} format.
     * @throws MatrixException if the errors during the conversion or LU-decomposition process appeared.
     */
    public LinearEquation(String matrixString) throws MatrixException {
        createRealMatrix(convertStringToDouble2DArray(matrixString));
        luDecomposition = new LUDecomposition(matrix);
    }

    /**
     * Converts {@code String} data to a 2-dimensional array of double.
     *
     * @param matrixString the input {@code String}.
     * @return the 2-dimensional double array.
     * @throws MatrixException if the errors during the conversion process appeared.
     */
    private double[][] convertStringToDouble2DArray(String matrixString) throws MatrixException {
        try {
            String lines[] = endOfLineChars(matrixString);
            double[][] m = new double[lines.length][lines.length];
            for (int i = 0; i < lines.length; i++) {
                m[i] = Arrays.stream(spaceChars(lines[i]))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
            }
            return m;
        } catch (Exception ex) {
            throw new MatrixException(ex);
        }
    }

    /**
     * Converts {@code String} data to a 2-dimensional array of double.
     *
     * @param vectorString the input {@code String}.
     * @return the 1-dimensional double array.
     * @throws VectorException if the errors during the conversion process appeared.
     */
    private double[] convertStringToDoubleArray(String vectorString) throws VectorException {
        try {
            return Arrays.stream(spaceChars(vectorString))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
        } catch (Exception ex) {
            throw new VectorException(ex);
        }
    }

    /**
     * Creates a {@link RealMatrix} using the data from the input array.
     *
     * @param matrixData input array.
     * @throws MatrixException if the errors during the creation process appeared.
     */
    private void createRealMatrix(double[][] matrixData) throws MatrixException {
        try {
            matrix = MatrixUtils.createRealMatrix(matrixData);
        } catch (Exception ex) {
            throw new MatrixException(ex);
        }
    }

    /**
     * Creates a {@link RealVector} using the data from the input array.
     *
     * @param vectorData input array.
     * @throws VectorException if data is empty or data is null.
     */
    private void createRealVector(double[] vectorData) throws VectorException {
        try {
            vector = MatrixUtils.createRealVector(vectorData);
        } catch (Exception ex) {
            throw new VectorException(ex);
        }
    }

    /**
     * Splits the {@code String} around the space character.
     *
     * @param s the input {@code String}.
     * @return string computed by splitting the {@code String} around the space character.
     */
    private String[] spaceChars(String s) {
        return s.split("\\s+");
    }

    /**
     * Splits the {@code String} around the end of the line.
     *
     * @param s the input {@code String}.
     * @return string computed by splitting the {@code String} around the end of the line.
     */
    private String[] endOfLineChars(String s) {
        return s.split("\\r?\\n");
    }

    /**
     * Returns the matrix.
     *
     * @return the matrix.
     */
    public RealMatrix getMatrix() {
        return matrix;
    }

    /**
     * Returns the vector.
     *
     * @return the vector.
     */
    public RealVector getVector() {
        return vector;
    }

    /**
     * Returns the matrix L of the decomposition.
     * <br>L is an lower triangular matrix.
     *
     * @return the L matrix.
     */
    public RealMatrix getLowerMatrix() {
        return luDecomposition.getL();
    }

    /**
     * Returns the matrix U of the decomposition.
     * <br>U is an upper triangular matrix.
     *
     * @return the U matrix.
     */
    public RealMatrix getUpperMatrix() {
        return luDecomposition.getU();
    }

    /**
     * Returns the pseudo-inverse of the decomposed matrix.
     *
     * @return pseudo-inverse matrix.
     * @throws MatrixException if the decomposed matrix is singular and the decomposition can not compute a pseudo-inverse.
     */
    public RealMatrix getInverseMatrix() throws MatrixException {
        try {
            return luDecomposition.getSolver().getInverse();
        } catch (Exception ex) {
            throw new MatrixException(ex);
        }
    }

    /**
     * Solves the linear equation A × X = B for matrices A and vector B.
     *
     * @return a vector X that minimizes the two norm of A × X - B.
     * @throws LinearEquationException if the matrices dimensions do not match or if the decomposed matrix is singular.
     */
    public RealVector getSolution() throws LinearEquationException {
        try {
            return luDecomposition.getSolver().solve(vector);
        } catch (Exception ex) {
            throw new LinearEquationException(ex);
        }
    }

    /**
     * Returns the determinant of the matrix.
     *
     * @return determinant of the matrix.
     */
    public double getDeterminant() {
        return luDecomposition.getDeterminant();
    }

    /**
     * Checks if the decomposed matrix is singular.
     *
     * @return true if the decomposed matrix is singular.
     */
    public boolean isSingularMatrix() {
        return !luDecomposition.getSolver().isNonSingular();
    }

    /**
     * Checks if the decomposition can compute.
     *
     * @return true if the decomposition can compute.
     */
    public boolean isLUDecomposition() {
        return luDecomposition.getL() != null;
    }
}
