package error;

/**
 * These exceptions should be retried to a certain point
 */
public class TransientException extends RuntimeException {
    public TransientException() {
    }

    public TransientException(String message) {
        super(message);
    }

    public TransientException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransientException(Throwable cause) {
        super(cause);
    }

    public TransientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
