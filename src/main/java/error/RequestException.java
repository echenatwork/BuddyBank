package error;

/**
 * Represents persistent exceptions that are due to bad request parameters, and are persistent.
 * i.e. unless the state of the db changes, the given request will not succeed no matter how many retries are done
 */
public class RequestException extends PersistentException {
    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
