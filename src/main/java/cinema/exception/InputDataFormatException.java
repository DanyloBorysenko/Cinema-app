package cinema.exception;

public class InputDataFormatException extends RuntimeException {
    public InputDataFormatException() {
        super("Input data can't be null");
    }
}
