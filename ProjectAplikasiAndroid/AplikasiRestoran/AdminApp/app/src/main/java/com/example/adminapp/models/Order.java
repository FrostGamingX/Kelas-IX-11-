package com.example.adminapp.models;

import java.util.List;
import java.util.Map;

public class Order {
    private String id;
    private String userId;
    private List<Map<String, Object>> products;  // Array of {product_id, quantity, price}
    private double totalPrice;
    private String address;
    private String status;
    private long timestamp;
    private String paymentMethod;

    public Order() {}

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<Map<String, Object>> getProducts() { return products; }
    public void setProducts(List<Map<String, Object>> products) { this.products = products; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}