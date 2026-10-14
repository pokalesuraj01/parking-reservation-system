package exception;

/** Raised when a requested parking slot cannot be reserved. */
public class SlotNotAvailableException extends Exception {
    public SlotNotAvailableException(String message) {
        super(message);
    }
}
