package assignment.exception;

/**
 * Signals that a calculation of linear equation has failed.
 *
 * @author Wiktor Florencki
 * @version 1.0
 * @see java.lang.Exception
 */
public class LinearEquationException extends Exception {

    /**
     * Constructs an {@code LinearEquationException} with the specified detail cause.
     *
     * @param cause the cause. (A null value is permitted,
     *              and indicates that the cause is nonexistent or unknown.)
     */
    public LinearEquationException(Throwable cause) {
        super(cause);
    }
}
