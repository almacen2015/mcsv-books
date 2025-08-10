package backend.exceptions.client;

public class ClientException extends RuntimeException {
    public static final String ERROR_NAME = "The name is required";
    public static final String ERROR_LASTNAME = "The lastname is required";
    public static final String ERROR_GENDER = "Invalid gender";
    public static final String ERROR_BIRTHDATE = "Invalid birthdate, you must be an adult";
    public static final String CLIENT_NOT_EXISTS = "Client not exists";
    public static final String CLIENT_ALREADY_EXISTS = "Client already exists";

    public ClientException(String message) {
        super(message);
    }
}
