package service;

import model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Registers customers and provides access to their in-memory records. */
public class CustomerService {
    private final Map<String, Customer> customers = new HashMap<>();

    /** Registers a customer and returns the new record with a generated ID. */
    public Customer registerCustomer(String name, String contactNumber) {
        String cleanedName = name == null ? "" : name.trim();
        String cleanedContact = contactNumber == null ? "" : contactNumber.trim();

        if (cleanedName.isEmpty()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (cleanedContact.isEmpty()) {
            throw new IllegalArgumentException("Customer contact number is required.");
        }

        String customerId = "CUST-" + UUID.randomUUID();
        Customer customer = new Customer(customerId, cleanedName, cleanedContact);
        customers.put(customerId, customer);
        return customer;
    }

    public Optional<Customer> findCustomerById(String customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
}
