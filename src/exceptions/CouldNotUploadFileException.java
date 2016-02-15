package exceptions;

public class CouldNotUploadFileException extends AzureException {
    public CouldNotUploadFileException() {
    }

    public CouldNotUploadFileException(String message) {
        super(message);
    }

    public CouldNotUploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotUploadFileException(Throwable cause) {
        super(cause);
    }
}
