package exceptions;

/**
 * Throw if the user provided an illegal price
 */
public class InvalidPriceException extends InvalidInputException{
    public InvalidPriceException() {
    }

    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException(String message, String param) {
        super(message, param);
    }
}
