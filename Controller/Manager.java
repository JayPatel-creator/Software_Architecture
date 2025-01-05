package Controller;

import Model.*;
import View.DepotGUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {

    public static void main(String[] args) {
        try {
            // Initialize application components
            ParcelMap parcelMap = initializeParcelMap("Parcels.csv");
            QueueOfCustomers customerQueue = initializeCustomerQueue("Custs.csv");

            // Setup GUI
            DepotGUI view = new DepotGUI();
            new DepotController(view, parcelMap, customerQueue);

            // Show GUI
            view.setVisible(true);

            // Log initialization
            Log.getInstance().addLogEntry("Application initialized successfully.");

            // Setup shutdown hook
            setupShutdownHook(parcelMap);

        } catch (IOException e) {
            handleInitializationError(e);
        }
    }


    private static ParcelMap initializeParcelMap(String filePath) throws IOException {
        ParcelMap parcelMap = new ParcelMap();
        parcelMap.loadParcelsFromFile(filePath);
        System.out.println("Parcels loaded successfully.");
        return parcelMap;
    }


    private static QueueOfCustomers initializeCustomerQueue(String filePath) throws IOException {
        QueueOfCustomers queueOfCustomers = new QueueOfCustomers();
        queueOfCustomers.loadCustomersFromFile(filePath);
        System.out.println("Customers loaded successfully.");
        return queueOfCustomers;
    }


    private static void setupShutdownHook(ParcelMap parcelMap) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // Create a StringBuilder to accumulate the report
                StringBuilder reportBuilder = new StringBuilder();

                // Add log entries
                reportBuilder.append("Application Logs:\n");
                reportBuilder.append(Log.getInstance().getLogs());
                reportBuilder.append("\n");

                // Write the report to the file
                Log.getInstance().writeLogToFile("Report.txt");

                // Append the collected/uncollected information to the same file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("Report.txt", true))) {
                    writer.write(reportBuilder.toString());
                }

                System.out.println("Report written to Report.txt");

            } catch (IOException e) {
                System.err.println("Failed to write report to file: " + e.getMessage());
            }
        }));
    }

    private static void handleInitializationError(IOException e) {
        System.err.println("Failed to initialize application: " + e.getMessage());
        Log.getInstance().addLogEntry("Initialization error: " + e.getMessage());
    }
}
