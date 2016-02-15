package exceptions;

public class CouldNotDeleteFileException extends AzureException {
    public CouldNotDeleteFileException() {
    }

    public CouldNotDeleteFileException(String message) {
        super(message);
    }

    public CouldNotDeleteFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotDeleteFileException(Throwable cause) {
        super(cause);
    }
}
