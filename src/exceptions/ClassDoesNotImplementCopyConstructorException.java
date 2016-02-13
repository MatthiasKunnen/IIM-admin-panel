package exceptions;

/**
 * Thrown if an object using the util.ImmutabilityHelper doesn't have a copy-constructor.
 * Created by matthias on 2016-02-13.
 */
public class ClassDoesNotImplementCopyConstructorException extends RuntimeException {
    public ClassDoesNotImplementCopyConstructorException() {
    }

    public ClassDoesNotImplementCopyConstructorException(String message) {
        super(message);
    }

    public ClassDoesNotImplementCopyConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassDoesNotImplementCopyConstructorException(Throwable cause) {
        super(cause);
    }
}
