package Controller;

import Model.*;

public class Worker {
    private static final double BASE_FEE = 10.0;
    private static final double WEIGHT_FEE_RATE = 0.5;
    private static final double DIMENSION_FEE_RATE = 0.1;
    private static final double DAYS_FEE_RATE = 0.2;

    // Calculate the fee for a given parcel
    public double calculateFee(Parcel parcel) {
        if (parcel == null) {
            throw new IllegalArgumentException("Parcel cannot be null");
        }

        double weightFee = parcel.getWeight() * WEIGHT_FEE_RATE;
        double dimensionFee = parcel.getLength() * parcel.getWidth() * parcel.getHeight() * DIMENSION_FEE_RATE;
        double daysFee = parcel.getDaysInDepot() * DAYS_FEE_RATE;

        return BASE_FEE + weightFee + dimensionFee + daysFee;
    }

    // Process a customer's parcel
    public void processCustomer(Customer customer, ParcelMap parcelMap) {
        if (customer == null || parcelMap == null) {
            throw new IllegalArgumentException("Customer and ParcelMap cannot be null");
        }

        Log log = Log.getInstance();
        Parcel parcel = parcelMap.getParcelById(customer.getParcelId());

        if (parcel != null) {
            double fee = calculateFee(parcel);
            log.addLogEntry(String.format("Processing Customer: %s | Parcel ID: %s | Fee: $%.2f",
                    customer.getName(), parcel.getParcelId(), fee));
            parcel.markCollected();
        } else {
            log.addLogEntry(String.format("Parcel not found for Customer: %s | Parcel ID: %s",
                    customer.getName(), customer.getParcelId()));
        }
    }

    // Log the removal of a customer
    public void removeCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        Log.getInstance().addLogEntry(String.format("Customer removed: %s", customer.getName()));
    }
}
