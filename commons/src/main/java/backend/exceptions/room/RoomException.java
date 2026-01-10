package backend.exceptions.room;

public class RoomException extends RuntimeException {
    public static final String ERROR_NUMBER = "The room number is invalid";
    public static final String ERROR_DESCRIPTION = "The description is invalid";
    public static final String ERROR_PRICE = "The price is invalid";
    public static final String ERROR_CAPACITY = "The capacity is invalid";
    public static final String ERROR_STATUS = "The room status is invalid";
    public static final String ERROR_TYPE = "The room type is invalid";

    public RoomException(String message) {
        super(message);
    }
}
