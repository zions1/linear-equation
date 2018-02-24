package assignment.exception;

/**
 * Signals that a calculation of linear equation has failed.
 *
 * @author Wiktor Florencki
 * @version 1.0
 * @see java.lang.Exception
 * @see assignment.exception.LinearEquationException
 */
public class VectorException extends LinearEquationException {

    /**
     * Constructs an {@code VectorException} with the specified detail cause.
     *
     * @param cause the cause. (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public VectorException(Throwable cause) {
        super(cause);
    }
}
