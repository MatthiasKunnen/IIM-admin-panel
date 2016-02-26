package exceptions;

/**
 *
 * Exception thrown when a login failed.
 * Use {@link #getLoginFailureCause} to get the cause of the failed login.
 * The default cause is {@link Cause#CREDENTIALS_INCORRECT}.
 * @author Matthias kunnen
 */
public class LoginException extends AdministratorException {
    private Cause cause = Cause.CREDENTIALS_INCORRECT;

    public enum Cause {
        ACCOUNT_SUSPENDED, CREDENTIALS_INCORRECT, ACCOUNT_LOCKED
    }

    public LoginException() {
    }

    public LoginException(Cause cause) {
        this.cause = cause;
    }

    public LoginException(String s) {
        super(s);
    }

    public LoginException(String s, Cause cause) {
        super(s);
        this.cause = cause;
    }

    public Cause getLoginFailureCause() {
        return cause;
    }
}
