package exceptions;

/**
 * Throw if the user provided an illegal price
 */
public class InvalidPriceException extends InvalidInputException {
    private Cause cause;

    public enum Cause {
        LOWER_THAN_ZERO, EXCEEDED_PRECISION, EXCEEDED_SCALE
    }

    public InvalidPriceException() {
    }

    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException(String message, Cause cause) {
        super(message);
        this.cause = cause;
    }

    public InvalidPriceException(Cause cause) {
        this.cause = cause;
    }

    public Cause getPriceInvalidityCause() {
        return cause;
    }
}
