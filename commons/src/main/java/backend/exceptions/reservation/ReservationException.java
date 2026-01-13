package backend.exceptions.reservation;

public class ReservationException extends RuntimeException {
    public static final String ROOM_NOT_AVAILABLE = "Room not available";
    public static final String RESERVATION_NOT_FOUND = "Reservation not found";

    public ReservationException(String message) {
        super(message);
    }
}
