package app.eurocars.exception;

public class CartServiceFeignCallException extends RuntimeException {
    public CartServiceFeignCallException(String message) {
        super(message);
    }
}
