package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.stream.Collectors;

public class QueueOfCustomers {
    private final Queue<Customer> customerQueue = new LinkedList<>();

    // Add a customer to the queue
    public void addCustomer(Customer customer) {
        if (customer != null) {
            customerQueue.add(customer);
        }
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return customerQueue.isEmpty();
    }

    // Get a list of all customers in the queue
    public List<Customer> getAllCustomers() {
        return new LinkedList<>(customerQueue); // Return a copy to protect the queue
    }

    // Create and add a new customer to the queue
    public void addNewCustomer(String name, String parcelId) {
        Customer newCustomer = new Customer(name, parcelId);
        addCustomer(newCustomer);
        Log.getInstance().addLogEntry("New customer added: " + name + " | Parcel ID: " + parcelId);
    }

    // Load customers from a file
    public void loadCustomersFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                    .map(this::parseCustomerFromLine)
                    .forEach(this::addCustomer);
        }
    }

    // Remove a customer from the queue by name and parcel ID
    public boolean removeCustomer(String name, String parcelId) {
        Customer customerToRemove = customerQueue.stream()
                .filter(customer -> customer.getName().equalsIgnoreCase(name) &&
                        customer.getParcelId().equalsIgnoreCase(parcelId))
                .findFirst()
                .orElse(null);

        if (customerToRemove != null) {
            customerQueue.remove(customerToRemove);
            Log.getInstance().addLogEntry("Customer removed: " + name + " | Parcel ID: " + parcelId);
            return true;
        }
        return false; // Customer not found
    }

    // Helper method to parse a Customer from a CSV line
    private Customer parseCustomerFromLine(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String name = parts[0].trim();
                String parcelId = parts[1].trim();
                return new Customer(name, parcelId);
            } else {
                Log.getInstance().addLogEntry("Invalid customer data: " + line);
            }
        } catch (Exception e) {
            Log.getInstance().addLogEntry("Error parsing customer data: " + line);
        }
        return null;
    }
}
