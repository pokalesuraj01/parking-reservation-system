package exception;

/** Raised when an operation requests a reservation ID that does not exist. */
public class ReservationNotFoundException extends Exception {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
