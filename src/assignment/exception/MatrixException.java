package assignment.exception;

/**
 * Signals that a calculation of linear equation has failed on the side of matrix.
 *
 * @author Wiktor Florencki
 * @version 1.0
 * @see java.lang.Exception
 * @see assignment.exception.LinearEquationException
 */
public class MatrixException extends LinearEquationException {

    /**
     * Constructs an {@code MatrixException} with the specified detail cause.
     *
     * @param cause the cause. (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public MatrixException(Throwable cause) {
        super(cause);
    }
}
