package exceptions;

public abstract class AzureException extends Exception{
    public AzureException() {
    }

    public AzureException(String message) {
        super(message);
    }

    public AzureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AzureException(Throwable cause) {
        super(cause);
    }
}
