package model;

/** A vehicle identified by its registration number and type. */
public class Vehicle {
    private String registrationNumber;
    private VehicleType type;

    public Vehicle(String registrationNumber, VehicleType type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vehicle{registrationNumber='" + registrationNumber + "', type=" + type + "}";
    }
}
