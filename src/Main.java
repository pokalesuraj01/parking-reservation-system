import exception.InvalidVehicleException;
import exception.ReservationNotFoundException;
import exception.SlotNotAvailableException;
import model.Customer;
import model.ParkingSlot;
import model.Reservation;
import model.Vehicle;
import model.VehicleType;
import repository.ParkingRepository;
import repository.ReservationRepository;
import service.CustomerService;
import service.ParkingService;
import service.ReservationService;
import util.InputUtil;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Console entry point for the parking reservation system. */
public class Main {
    private final InputUtil input = new InputUtil();
    private final CustomerService customerService = new CustomerService();
    private final ParkingService parkingService = new ParkingService(new ParkingRepository());
    private final ReservationService reservationService = new ReservationService(
            customerService, parkingService, new ReservationRepository());

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        parkingService.initializeSlots(5, 10, 3);
        System.out.println("Initialized 5 bike, 10 car, and 3 SUV parking slots.");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readMenuChoice("Enter your choice: ");
            try {
                switch (choice) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        viewAllSlots();
                        break;
                    case 3:
                        viewAvailableSlots();
                        break;
                    case 4:
                        bookSlot();
                        break;
                    case 5:
                        viewAllReservations();
                        break;
                    case 6:
                        searchReservation();
                        break;
                    case 7:
                        cancelReservation();
                        break;
                    case 8:
                        completeReservation();
                        break;
                    case 9:
                        viewCustomerReservations();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Goodbye.");
                        break;
                    default:
                        System.out.println("Invalid menu choice. Choose a number from 0 to 9.");
                }
            } catch (SlotNotAvailableException | InvalidVehicleException | ReservationNotFoundException
                     | IllegalArgumentException | IllegalStateException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("========== PARKING RESERVATION SYSTEM ==========");
        System.out.println("1. Register Customer");
        System.out.println("2. View All Parking Slots");
        System.out.println("3. View Available Parking Slots");
        System.out.println("4. Book Parking Slot");
        System.out.println("5. View All Reservations");
        System.out.println("6. Search Reservation");
        System.out.println("7. Cancel Reservation");
        System.out.println("8. Complete Reservation");
        System.out.println("9. View Customer Reservations");
        System.out.println("0. Exit");
    }

    private void registerCustomer() {
        String name = input.readString("Customer name: ");
        String contactNumber = input.readString("Contact number: ");
        Customer customer = customerService.registerCustomer(name, contactNumber);
        System.out.println("Customer registered: " + customer);
    }

    private void viewAllSlots() {
        List<ParkingSlot> slots = parkingService.getAllSlots();
        if (slots.isEmpty()) {
            System.out.println("No parking slots are configured.");
            return;
        }
        slots.forEach(System.out::println);
    }

    private void viewAvailableSlots() {
        VehicleType vehicleType = readVehicleType();
        List<ParkingSlot> slots = parkingService.getAvailableSlots(vehicleType);
        if (slots.isEmpty()) {
            System.out.println("No available " + vehicleType + " slots.");
            return;
        }
        slots.forEach(System.out::println);
    }

    private void bookSlot() throws SlotNotAvailableException, InvalidVehicleException {
        String customerId = input.readString("Customer ID: ");
        int slotId = input.readInt("Parking slot ID: ");
        String registrationNumber = input.readString("Vehicle registration number: ");
        VehicleType vehicleType = readVehicleType();

        Reservation reservation = reservationService.createReservation(
                customerId, slotId, new Vehicle(registrationNumber, vehicleType));
        System.out.println("Reservation created: " + reservation);
    }

    private void viewAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations have been made.");
            return;
        }
        reservations.forEach(System.out::println);
    }

    private void searchReservation() {
        String reservationId = input.readString("Reservation ID: ");
        Optional<Reservation> reservation = reservationService.findReservationById(reservationId);
        System.out.println(reservation.map(Object::toString).orElse("Reservation not found."));
    }

    private void cancelReservation() throws ReservationNotFoundException {
        String reservationId = input.readString("Reservation ID to cancel: ");
        reservationService.cancelReservation(reservationId);
        System.out.println("Reservation cancelled and parking slot released.");
    }

    private void completeReservation() throws ReservationNotFoundException {
        String reservationId = input.readString("Reservation ID to complete: ");
        reservationService.completeReservation(reservationId);
        System.out.println("Reservation completed and parking slot released.");
    }

    private void viewCustomerReservations() {
        String customerId = input.readString("Customer ID: ");
        if (!customerService.findCustomerById(customerId).isPresent()) {
            System.out.println("Customer " + customerId + " is not registered.");
            return;
        }
        List<Reservation> reservations = reservationService.getReservationsForCustomer(customerId);
        if (reservations.isEmpty()) {
            System.out.println("This customer has no reservations.");
            return;
        }
        reservations.forEach(System.out::println);
    }

    private VehicleType readVehicleType() {
        while (true) {
            String value = input.readString("Vehicle type (BIKE, CAR, SUV): ");
            try {
                return VehicleType.valueOf(value.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid vehicle type. Enter BIKE, CAR, or SUV.");
            }
        }
    }
}
