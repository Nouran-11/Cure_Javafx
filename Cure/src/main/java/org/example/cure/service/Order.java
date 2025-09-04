package org.example.cure.service;


import org.example.cure.model.PharmacyItem;

import java.util.*;

import java.time.LocalDateTime;


public class Order {
    private static int orderCounter = 0;
    private int orderId;
    private List<PharmacyItem> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private String customerName;


    public Order(String customerName) {
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now();
        this.orderId = ++orderCounter;
    }

    public void addItem(PharmacyItem item) { items.add(item); }
    public void removeItem(PharmacyItem item) { items.remove(item); }
    public void sortItems() { Collections.sort(items); }
    public double calculateTotal() {
        return items.stream().mapToDouble(PharmacyItem::getPrice).sum();
    }

    public List<PharmacyItem> getItems() { return items; }
    public PharmacyItem findItemByName(String name) {
        return items.stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public String getCustomerName() { return customerName; }
    public int getOrderId() { return orderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }


    @Override
    public String toString() {
        return "Order #" + orderId + " for " + customerName +
                " | Items: " + items.size() + " | Total: $" + calculateTotal();
    }
}
