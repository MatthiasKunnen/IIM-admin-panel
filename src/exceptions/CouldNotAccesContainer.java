package exceptions;

public class CouldNotAccesContainer extends AzureException {
    public CouldNotAccesContainer() {
    }

    public CouldNotAccesContainer(String message) {
        super(message);
    }

    public CouldNotAccesContainer(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotAccesContainer(Throwable cause) {
        super(cause);
    }
}
