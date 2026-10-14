package model;

/** A parking space that supports one vehicle category. */
public class ParkingSlot {
    private int slotId;
    private VehicleType supportedVehicleType;
    private boolean available;

    public ParkingSlot(int slotId, VehicleType supportedVehicleType, boolean available) {
        this.slotId = slotId;
        this.supportedVehicleType = supportedVehicleType;
        this.available = available;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public VehicleType getSupportedVehicleType() {
        return supportedVehicleType;
    }

    public void setSupportedVehicleType(VehicleType supportedVehicleType) {
        this.supportedVehicleType = supportedVehicleType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "ParkingSlot{slotId=" + slotId + ", supportedVehicleType=" + supportedVehicleType
                + ", available=" + available + "}";
    }
}
