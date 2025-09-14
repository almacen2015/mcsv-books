package backend.exceptions.client;

public class ClientException extends RuntimeException {
    public static final String ERROR_NAME = "The name is required";
    public static final String ERROR_LASTNAME = "The lastname is required";
    public static final String ERROR_GENDER = "Invalid gender";
    public static final String ERROR_BIRTHDATE = "Invalid birthdate, you must be an adult";
    public static final String CLIENT_NOT_EXISTS = "Client not exists";
    public static final String CLIENT_ALREADY_EXISTS = "Client already exists";
    public static final String ERROR_DOCUMENT_NUMBER = "Document number is required";
    public static final String ERROR_DNI = "DNI must have 8 digits";
    public static final String ERROR_CE = "CE must have 9 digits";
    public static final String ERROR_DOCUMENT_TYPE = "Invalid Document Type";

    public ClientException(String message) {
        super(message);
    }
}
