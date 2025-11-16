package app.eurocars.exception;

public class EngineNotFound extends RuntimeException {
    public EngineNotFound(String message) {
        super(message);
    }
}
