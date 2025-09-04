package org.example.cure.model;

import java.util.List;

public class Recipe {
    private final String patientName;
    private final List<PharmacyItem> prescribedItems;

    public Recipe(String patientName, List<PharmacyItem> prescribedItems) {
        this.patientName = patientName;
        this.prescribedItems = prescribedItems;
    }

    public double getTotalCost() {
        return prescribedItems.stream().mapToDouble(PharmacyItem::getPrice).sum();
    }

    public String getDetails() {
        StringBuilder sb = new StringBuilder("Recipe for " + patientName + ":\n");
        for (PharmacyItem item : prescribedItems) {
            sb.append("- ").append(item.toString()).append("\n");
        }
        sb.append("Total: $").append(getTotalCost());
        return sb.toString();
    }
}