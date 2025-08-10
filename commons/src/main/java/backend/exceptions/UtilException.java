package backend.exceptions;

public class UtilException extends RuntimeException {
    public static final String ID_NOT_VALID = "Id is not valid";

    public UtilException(String message) {
        super(message);
    }
}
