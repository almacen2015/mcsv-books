package backend.exceptions.reservation;

public class ReservationException extends RuntimeException {
    public static final String ROOM_NOT_AVAILABLE = "Room Not Available";

    public ReservationException(String message) {
        super(message);
    }
}
