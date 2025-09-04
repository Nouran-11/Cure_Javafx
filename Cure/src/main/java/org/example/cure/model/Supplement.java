package org.example.cure.model;

public class Supplement extends PharmacyItem {
    private String description;

    public Supplement(int id, String name, double price, String description) {
        super(id, name, price);
        this.description = description;
    }

    @Override
    public String getDetails() {
        return name + " (" + description + ")";
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}