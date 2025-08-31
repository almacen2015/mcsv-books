package backend.exceptions;

public class UtilException extends RuntimeException {
    public static final String ID_NOT_VALID = "Id is not valid";
    public static final String DATE_NOT_VALID = "Invalid date format";

    public UtilException(String message) {
        super(message);
    }
}
