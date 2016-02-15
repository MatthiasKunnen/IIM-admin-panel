package exceptions;

public class CouldNotUploadFileException extends Exception {
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

    public CouldNotUploadFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
