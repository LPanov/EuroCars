package app.eurocars.exception;

public class NotMatchingPasswords extends RuntimeException {
    public NotMatchingPasswords(String message) {
        super(message);
    }

}
