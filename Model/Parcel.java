package Model;

public class Parcel {
    private String parcelId;
    private double length, width, height, weight;
    private double daysInDepot;
    private boolean Collected;

    public Parcel(String parcelId, double length, double width, double height, double daysInDepot, double weight) {
        this.parcelId = parcelId;
        this.length = length;
        this.width = width;
        this.height = height;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.Collected = false;
    }

    public String getParcelId() { return parcelId; }
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public double getDaysInDepot() { return daysInDepot; }
    public boolean Collected() { return Collected; }
    public void setCollected(boolean collected) {
        this.Collected = collected;
    }
    public void markCollected() { this.Collected = true; }

    @Override
    public String toString() {
        return "Parcel ID: " + parcelId + "\nThe Dimensions: " + length + " x " + width + " x " + height +
                "\nThe Weight: " + weight + " kg" + "\nThe Days in Depot: " + daysInDepot;
    }
}
