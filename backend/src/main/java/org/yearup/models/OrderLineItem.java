package org.yearup.models;

public class OrderLineItem {
    private int orderLineItemId;
    private int orderId;
    private int productId;
    private double salesPrice;
    private int quantity;
    private double discount;

    public OrderLineItem() {
    }

    public OrderLineItem(int orderLineItemId, int orderId, int productId, double salesPrice, int quantity, double discount) {
        this.orderLineItemId = orderLineItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    // Getters and Setters
    public int getOrderLineItemId() { return orderLineItemId; }
    public void setOrderLineItemId(int orderLineItemId) { this.orderLineItemId = orderLineItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public double getSalesPrice() { return salesPrice; }
    public void setSalesPrice(double salesPrice) { this.salesPrice = salesPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}
