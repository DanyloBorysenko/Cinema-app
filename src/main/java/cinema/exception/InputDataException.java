package cinema.exception;

public class InputDataException extends RuntimeException {
    public InputDataException() {
        super("Input data can't be null");
    }
}
