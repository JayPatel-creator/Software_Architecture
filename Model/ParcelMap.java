package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParcelMap {
    private final Map<String, Parcel> parcels = new HashMap<>();

    // Add a parcel to the map
    public void addParcel(Parcel parcel) {
        if (parcel != null && parcel.getParcelId() != null) {
            parcels.put(parcel.getParcelId(), parcel);
        }
    }

    // Create and add a new parcel to the map
    public void addNewParcel(String id, double length, double width, double height, int daysInDepot, double weight) {
        Parcel parcel = new Parcel(id, length, width, height, daysInDepot, weight);
        addParcel(parcel);
        Log.getInstance().addLogEntry("New parcel added: " + id);
    }

    // Load parcels from a file
    public void loadParcelsFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                    .map(this::parseParcelFromLine)
                    .forEach(this::addParcel);
        }
    }

    // Remove a parcel by ID
    public void removeParcel(String parcelId) {
        parcels.remove(parcelId);
    }

    // Retrieve all parcels
    public Map<String, Parcel> getAllParcels() {
        return new HashMap<>(parcels);
    }

    // Retrieve a parcel by its ID
    public Parcel getParcelById(String parcelId) {
        return parcels.get(parcelId);
    }

    // Helper method to parse a Parcel from a CSV line
    private Parcel parseParcelFromLine(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                String id = parts[0].trim();
                double length = Double.parseDouble(parts[1].trim());
                double width = Double.parseDouble(parts[2].trim());
                double height = Double.parseDouble(parts[3].trim());
                int daysInDepot = Integer.parseInt(parts[4].trim());
                double weight = Double.parseDouble(parts[5].trim());
                return new Parcel(id, length, width, height, daysInDepot, weight);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            Log.getInstance().addLogEntry("Invalid parcel data: " + line);
        }
        return null;
    }
}
