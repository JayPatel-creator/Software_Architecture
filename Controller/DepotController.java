package Controller;

import Model.*;
import View.DepotGUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public class DepotController {
    private DepotGUI view;
    private ParcelMap parcelMap;
    private QueueOfCustomers customerQueue;
    private Worker worker;

    public DepotController(DepotGUI view, ParcelMap parcelMap, QueueOfCustomers customerQueue) {
        this.view = view;
        this.parcelMap = parcelMap;
        this.customerQueue = customerQueue;
        this.worker = new Worker();

        initializeView();
        registerEventHandlers();
    }

    // Initialize the view and update displays
    private void initializeView() {
        updateParcelsDisplay();
        updateCustomersDisplay();
    }

    // Register UI event handlers
    private void registerEventHandlers() {
        addActionListener(view.getProcessCustomerButton(), e -> processCustomer());
        addActionListener(view.getAddCustomerButton(), e -> addNewCustomer());
        addActionListener(view.getAddParcelButton(), e -> addNewParcel());
        addActionListener(view.getSearchParcelButton(), e -> searchParcel());
        addActionListener(view.getUpdateParcelStatusButton(), e -> updateParcelStatus());
        addActionListener(view.getRemoveCustomerButton(), e -> removeCustomer());

        // Add an action listener for sorting parcels
        view.getSortComboBox().addActionListener(e -> sortParcels());
    }

    private void addActionListener(JButton button, ActionListener action) {
        if (button != null) {
            button.addActionListener(action);
        }
    }

    private void sortParcels() {
        boolean isAscending = view.getSortComboBox().getSelectedItem().equals("Ascending");

        // Sort parcels based on "Days in Depot"
        var sortedParcels = parcelMap.getAllParcels().values().stream()
                .sorted((p1, p2) -> {
                    // Sort based on Days in Depot using Double.compare for accurate comparison of doubles
                    if (isAscending) {
                        return Double.compare(p1.getDaysInDepot(), p2.getDaysInDepot());
                    } else {
                        return Double.compare(p2.getDaysInDepot(), p1.getDaysInDepot());
                    }
                })
                .map(Parcel::toString)
                .reduce("", (a, b) -> a + b + "\n");

        // Update the parcel list display
        view.getParcelTextArea().setText(sortedParcels);
    }

    // Update the display of parcels in the view
    private void updateParcelsDisplay() {
        String parcelData = parcelMap.getAllParcels().values().stream()
                .map(Parcel::toString)
                .reduce("", (a, b) -> a + b + "\n");
        view.getParcelTextArea().setText(parcelData);
    }

    // Update the display of customers in the view
    private void updateCustomersDisplay() {
        String customerData = customerQueue.getAllCustomers().stream()
                .map(Customer::toString)
                .reduce("", (a, b) -> a + b + "\n");
        view.getCustomerTextArea().setText(customerData);
    }

    // Process the next customer in the queue
    private void processCustomer() {
        if (customerQueue.isEmpty()) {
            showMessage("No customers to process.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Customer selectedCustomer = (Customer) JOptionPane.showInputDialog(
                view,
                "Select a customer to process:",
                "Process Customer",
                JOptionPane.PLAIN_MESSAGE,
                null,
                customerQueue.getAllCustomers().toArray(),
                null
        );

        if (selectedCustomer == null) {
            showMessage("No customer selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        worker.processCustomer(selectedCustomer, parcelMap);
        customerQueue.removeCustomer(selectedCustomer.getName(), selectedCustomer.getParcelId());

        Parcel parcel = parcelMap.getParcelById(selectedCustomer.getParcelId());
        if (parcel != null && parcel.Collected()) {
            parcelMap.removeParcel(parcel.getParcelId());
        }

        showMessage("Customer processed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Use the Worker class to calculate the fee
        double fee = worker.calculateFee(parcel);

        // Display the fee
        JOptionPane.showMessageDialog(
                view,
                "Processing Customer: " + selectedCustomer.getName() +
                        "\nParcel ID: " + parcel.getParcelId() +
                        "\nFee: $" + String.format("%.2f", fee),
                "Customer Fees",
                JOptionPane.INFORMATION_MESSAGE
        );
        updateParcelsDisplay();
        updateCustomersDisplay();
    }

    // Add a new customer to the queue
    private void addNewCustomer() {
        String name = promptInput("Enter Customer Name:");
        String parcelId = promptInput("Enter Parcel ID:");
        if (name != null && parcelId != null) {
            customerQueue.addNewCustomer(name, parcelId);
            updateCustomersDisplay();
        }
    }

    // Add a new parcel to the map
    private void addNewParcel() {
        try {
            String id = promptInput("Enter Parcel ID:");
            double length = Double.parseDouble(promptInput("Enter Length:"));
            double width = Double.parseDouble(promptInput("Enter Width:"));
            double height = Double.parseDouble(promptInput("Enter Height:"));
            int daysInDepot = Integer.parseInt(promptInput("Enter Days in Depot:"));
            double weight = Double.parseDouble(promptInput("Enter Weight:"));

            parcelMap.addNewParcel(id, length, width, height, daysInDepot, weight);
            updateParcelsDisplay();
        } catch (NumberFormatException e) {
            showMessage("Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Search for a parcel by ID
    private void searchParcel() {
        String id = view.getSearchParcelField().getText().trim();
        Parcel parcel = parcelMap.getParcelById(id);
        if (parcel != null) {
            showMessage(parcel.toString(), "Parcel Found", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("Parcel not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update the status of a parcel
    private void updateParcelStatus() {
        String id = promptInput("Enter Parcel ID to Update Status:");
        Parcel parcel = parcelMap.getParcelById(id);

        if (parcel != null) {
            String[] options = {"Collected", "Not Collected"};
            int choice = JOptionPane.showOptionDialog(
                    view,
                    "Choose the new status for the parcel:",
                    "Update Parcel Status",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                parcel.setCollected(true);
                parcelMap.removeParcel(id);
                showMessage("Parcel marked as collected and removed from the list.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (choice == 1) {
                parcel.setCollected(false);
                Log.getInstance().addLogEntry("Parcel not collected: " + parcel.getParcelId());
                showMessage("Parcel status updated to 'Not Collected'.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            showMessage("Parcel not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        updateParcelsDisplay();
    }

    // Remove a customer from the queue
    private void removeCustomer() {
        String name = promptInput("Enter Customer Name:");
        String parcelId = promptInput("Enter Parcel ID:");
        if (name != null && parcelId != null) {
            boolean removed = customerQueue.removeCustomer(name, parcelId);
            String message = removed ? "Customer removed successfully." : "Customer not found.";
            showMessage(message, removed ? "Success" : "Error", removed ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            updateCustomersDisplay();
        }
    }

    // Helper method to show messages
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(view, message, title, messageType);
    }

    // Helper method to prompt user input
    private String promptInput(String message) {
        return JOptionPane.showInputDialog(view, message);
    }
}
