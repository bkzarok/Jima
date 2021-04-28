package bkz.app.jima.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class RegisterResult<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private RegisterResult() {
    }

    @Override
    public String toString() {
        if (this instanceof RegisterResult.Success) {
            RegisterResult.Success success = (RegisterResult.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof RegisterResult.Error) {
            RegisterResult.Error error = (RegisterResult.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends RegisterResult {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends RegisterResult {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}