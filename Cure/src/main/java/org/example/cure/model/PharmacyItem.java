package org.example.cure.model;

public abstract class PharmacyItem implements Comparable<PharmacyItem> {
    protected int id;
    protected String name;
    protected double price;

    public PharmacyItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public abstract String getDetails();

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) throws InvalidPriceException {
        if (price < 0) {
            throw new InvalidPriceException("Price cannot be negative.");
        }
        this.price = price;
    }

    @Override
    public int compareTo(PharmacyItem other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return getDetails() + " - $" + price;
    }
}