package service;

import exception.SlotNotAvailableException;
import model.ParkingSlot;
import model.VehicleType;
import repository.ParkingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Initializes parking slots and manages their availability. */
public class ParkingService {
    private final ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        if (parkingRepository == null) {
            throw new IllegalArgumentException("Parking repository is required.");
        }
        this.parkingRepository = parkingRepository;
    }

    /** Creates the requested number of slots for each vehicle category. */
    public void initializeSlots(int bikeSlots, int carSlots, int suvSlots) {
        if (bikeSlots < 0 || carSlots < 0 || suvSlots < 0) {
            throw new IllegalArgumentException("Slot counts cannot be negative.");
        }
        if (!parkingRepository.findAll().isEmpty()) {
            throw new IllegalStateException("Parking slots have already been initialized.");
        }

        int nextSlotId = 1;
        nextSlotId = addSlots(VehicleType.BIKE, bikeSlots, nextSlotId);
        nextSlotId = addSlots(VehicleType.CAR, carSlots, nextSlotId);
        addSlots(VehicleType.SUV, suvSlots, nextSlotId);
    }

    private int addSlots(VehicleType type, int count, int firstId) {
        for (int index = 0; index < count; index++) {
            parkingRepository.save(new ParkingSlot(firstId + index, type, true));
        }
        return firstId + count;
    }

    public List<ParkingSlot> getAllSlots() {
        return parkingRepository.findAll();
    }

    public List<ParkingSlot> getAvailableSlots(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type is required.");
        }
        return parkingRepository.findAll().stream()
                .filter(ParkingSlot::isAvailable)
                .filter(slot -> slot.getSupportedVehicleType() == vehicleType)
                .collect(Collectors.toList());
    }

    public Optional<ParkingSlot> findSlotById(int slotId) {
        return parkingRepository.findById(slotId);
    }

    /** Marks an available slot as occupied. */
    public void occupySlot(int slotId) throws SlotNotAvailableException {
        ParkingSlot slot = parkingRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotAvailableException("Parking slot " + slotId + " does not exist."));
        if (!slot.isAvailable()) {
            throw new SlotNotAvailableException("Parking slot " + slotId + " is not available.");
        }
        slot.setAvailable(false);
        parkingRepository.update(slot);
    }

    /** Marks a slot as available again after its reservation ends. */
    public void releaseSlot(int slotId) {
        ParkingSlot slot = parkingRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Parking slot " + slotId + " does not exist."));
        slot.setAvailable(true);
        parkingRepository.update(slot);
    }
}
