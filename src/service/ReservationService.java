package service;

import exception.InvalidVehicleException;
import exception.ReservationNotFoundException;
import exception.SlotNotAvailableException;
import model.ParkingSlot;
import model.Reservation;
import model.ReservationStatus;
import model.Vehicle;
import model.VehicleType;
import repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Applies reservation business rules and coordinates customers, slots, and records. */
public class ReservationService {
    private static final Pattern REGISTRATION_PATTERN = Pattern.compile("[A-Z0-9-]{5,15}");

    private final CustomerService customerService;
    private final ParkingService parkingService;
    private final ReservationRepository reservationRepository;

    public ReservationService(CustomerService customerService, ParkingService parkingService,
                              ReservationRepository reservationRepository) {
        if (customerService == null || parkingService == null || reservationRepository == null) {
            throw new IllegalArgumentException("Customer, parking, and reservation services/repository are required.");
        }
        this.customerService = customerService;
        this.parkingService = parkingService;
        this.reservationRepository = reservationRepository;
    }

    /** Validates the booking and returns the newly created active reservation. */
    public Reservation createReservation(String customerId, int slotId, Vehicle vehicle)
            throws SlotNotAvailableException, InvalidVehicleException {
        if (vehicle == null) {
            throw new InvalidVehicleException("Vehicle details are required.");
        }
        String registrationNumber = vehicle.getRegistrationNumber() == null
                ? "" : vehicle.getRegistrationNumber().trim().toUpperCase(Locale.ROOT);
        if (!REGISTRATION_PATTERN.matcher(registrationNumber).matches()
                || !registrationNumber.matches(".*[A-Z0-9].*")) {
            throw new InvalidVehicleException("Registration number must be 5-15 characters using letters, numbers, or hyphens.");
        }
        VehicleType vehicleType = vehicle.getType();
        if (vehicleType == null) {
            throw new InvalidVehicleException("Vehicle type is required.");
        }
        vehicle.setRegistrationNumber(registrationNumber);

        if (customerId == null || !customerService.findCustomerById(customerId).isPresent()) {
            throw new IllegalArgumentException("Customer " + customerId + " is not registered.");
        }

        Optional<ParkingSlot> requestedSlot = parkingService.findSlotById(slotId);
        if (!requestedSlot.isPresent()) {
            throw new SlotNotAvailableException("Parking slot " + slotId + " does not exist.");
        }
        ParkingSlot slot = requestedSlot.get();
        if (slot.getSupportedVehicleType() != vehicleType) {
            throw new SlotNotAvailableException("Parking slot " + slotId + " does not support " + vehicleType + ".");
        }
        if (!slot.isAvailable()) {
            throw new SlotNotAvailableException("Parking slot " + slotId + " is not available.");
        }

        boolean vehicleAlreadyParked = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACTIVE)
                .anyMatch(reservation -> reservation.getVehicle().getRegistrationNumber()
                        .equalsIgnoreCase(registrationNumber));
        if (vehicleAlreadyParked) {
            throw new InvalidVehicleException("This vehicle already has an active reservation.");
        }

        Reservation reservation = new Reservation(
                "RES-" + UUID.randomUUID(), customerId, slotId, vehicle,
                LocalDateTime.now(), ReservationStatus.ACTIVE);

        // All validation is complete before the slot changes. Roll it back if saving fails.
        parkingService.occupySlot(slotId);
        try {
            reservationRepository.save(reservation);
        } catch (RuntimeException exception) {
            parkingService.releaseSlot(slotId);
            throw exception;
        }
        return reservation;
    }

    public void cancelReservation(String reservationId) throws ReservationNotFoundException {
        Reservation reservation = getReservationOrThrow(reservationId);
        ensureActive(reservation, "cancel");
        parkingService.releaseSlot(reservation.getSlotId());
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.update(reservation);
    }

    public void completeReservation(String reservationId) throws ReservationNotFoundException {
        Reservation reservation = getReservationOrThrow(reservationId);
        ensureActive(reservation, "complete");
        parkingService.releaseSlot(reservation.getSlotId());
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.update(reservation);
    }

    private void ensureActive(Reservation reservation, String operation) {
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            String pastTense = "cancel".equals(operation) ? "cancelled" : "completed";
            throw new IllegalStateException("Reservation " + reservation.getReservationId()
                    + " is already " + reservation.getStatus().name().toLowerCase(Locale.ROOT)
                    + " and cannot be " + pastTense + " again.");
        }
    }

    private Reservation getReservationOrThrow(String reservationId) throws ReservationNotFoundException {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(
                        "Reservation " + reservationId + " was not found."));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findReservationById(String reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public List<Reservation> getReservationsForCustomer(String customerId) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
}
