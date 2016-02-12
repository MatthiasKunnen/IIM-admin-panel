package exceptions;

/**
 * Throw this exception when the user has given an invalid value.
 * DO NOT use this exception for data that is NOT provided by the user. USE IllegalArgumentException instead.
 *
 * @author matthias
 */
public abstract class InvalidInputException extends Exception {
    private String param;

    public String getParam() {
        return param;
    }

    public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, String param) {
        super(message);
        this.param = param;
    }
}
