package repository;

import model.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** In-memory storage and lookup operations for reservations. */
public class ReservationRepository {
    private final Map<String, Reservation> reservations = new HashMap<>();

    /** Adds a reservation, rejecting an ID that is already in use. */
    public void save(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null.");
        }
        String reservationId = reservation.getReservationId();
        if (reservations.containsKey(reservationId)) {
            throw new IllegalArgumentException("A reservation with ID " + reservationId + " already exists.");
        }
        reservations.put(reservationId, reservation);
    }

    public Optional<Reservation> findById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    /** Replaces an existing reservation; it does not create a new record. */
    public void update(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null.");
        }
        String reservationId = reservation.getReservationId();
        if (!reservations.containsKey(reservationId)) {
            throw new IllegalArgumentException("Reservation " + reservationId + " does not exist.");
        }
        reservations.put(reservationId, reservation);
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }
}
