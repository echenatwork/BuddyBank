package error;

/**
 * Represents exceptions that are "permanent", i.e. errors that will always be thrown, so there is no point in
 * retrying
 */
public class PersistentException extends RuntimeException {

    public PersistentException() {
    }

    public PersistentException(String message) {
        super(message);
    }

    public PersistentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistentException(Throwable cause) {
        super(cause);
    }

    public PersistentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
