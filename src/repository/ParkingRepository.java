package repository;

import model.ParkingSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** In-memory storage and lookup operations for parking slots. */
public class ParkingRepository {
    private final Map<Integer, ParkingSlot> slots = new HashMap<>();

    /** Adds a slot, rejecting an ID that is already in use. */
    public void save(ParkingSlot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Parking slot cannot be null.");
        }
        if (slots.containsKey(slot.getSlotId())) {
            throw new IllegalArgumentException("A parking slot with ID " + slot.getSlotId() + " already exists.");
        }
        slots.put(slot.getSlotId(), slot);
    }

    public Optional<ParkingSlot> findById(int slotId) {
        return Optional.ofNullable(slots.get(slotId));
    }

    /** Replaces an existing slot record; it does not create a new record. */
    public void update(ParkingSlot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Parking slot cannot be null.");
        }
        if (!slots.containsKey(slot.getSlotId())) {
            throw new IllegalArgumentException("Parking slot " + slot.getSlotId() + " does not exist.");
        }
        slots.put(slot.getSlotId(), slot);
    }

    public List<ParkingSlot> findAll() {
        return new ArrayList<>(slots.values());
    }
}
