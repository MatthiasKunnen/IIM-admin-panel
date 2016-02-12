package exceptions;

/**
 * Throw if the user provided an incorrect email address
 * @author matthias
 */
public class InvalidEmailException extends InvalidInputException {

    public InvalidEmailException() {
    }

    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, String param) {
        super(message, param);
    }
}
