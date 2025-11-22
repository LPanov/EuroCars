package app.eurocars.exception;

public class ModelNotFound extends RuntimeException {
    public ModelNotFound(String message) {
        super(message);
    }
}
