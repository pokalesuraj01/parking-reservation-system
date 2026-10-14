package model;

import java.time.LocalDateTime;

/** A customer's reservation of a parking slot for a vehicle. */
public class Reservation {
    private String reservationId;
    private String customerId;
    private int slotId;
    private Vehicle vehicle;
    private LocalDateTime creationTime;
    private ReservationStatus status;

    public Reservation(String reservationId, String customerId, int slotId, Vehicle vehicle,
                       LocalDateTime creationTime, ReservationStatus status) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.slotId = slotId;
        this.vehicle = vehicle;
        this.creationTime = creationTime;
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{reservationId='" + reservationId + "', customerId='" + customerId
                + "', slotId=" + slotId + ", vehicle=" + vehicle + ", creationTime=" + creationTime
                + ", status=" + status + "}";
    }
}
