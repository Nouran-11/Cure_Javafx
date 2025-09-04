package org.example.cure.model;

public  class Medicine extends PharmacyItem {
    private String prescriptionRequired;

    public Medicine(int id, String name, double price, String prescriptionRequired) {
        super(id, name, price);
        this.prescriptionRequired = prescriptionRequired;
    }

    public void setPrescription(String prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }
    public String getPrescription() {
        return prescriptionRequired;
    }

    @Override
    public String getDetails() {
        return name + " (Prescription: " + prescriptionRequired + ")";
    }
}