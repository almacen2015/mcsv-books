package backend.exceptions.client;

public class ClientException extends RuntimeException {
    public static final String ERROR_NAME = "The name is required";
    public static final String ERROR_LASTNAME = "The lastname is required";
    public static final String ERROR_GENDER = "Invalid gender";
    public static final String ERROR_BIRTHDATE = "Invalid birthdate, you must be an adult";

    public ClientException(String message) {
        super(message);
    }
}
