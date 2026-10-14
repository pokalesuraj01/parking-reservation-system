package exception;

/** Raised when vehicle details are missing or invalid. */
public class InvalidVehicleException extends Exception {
    public InvalidVehicleException(String message) {
        super(message);
    }
}
