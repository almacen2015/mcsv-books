package backend.exceptions.client;

public class ClientException extends RuntimeException {
    public static final String ERROR_NAME = "The name is required";

    public ClientException(String message) {
        super(message);
    }
}
