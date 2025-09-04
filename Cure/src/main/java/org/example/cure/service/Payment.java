package org.example.cure.service;

public class Payment implements Payable {
    private Order order;

    public Payment(Order order) {
        this.order = order;
    }

    @Override
    public double calculateTotal() {
        return order.calculateTotal();
    }
}
