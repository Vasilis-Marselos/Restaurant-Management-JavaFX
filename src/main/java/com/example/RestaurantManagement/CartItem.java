package com.example.RestaurantManagement;

public class CartItem {
    private String dishName;
    private double unitPrice;
    private int quantity;
    private String description;
    private String extras;
    private String category;

    private static final double VAT_RATE = 0.24;

    // Constructor που αρχικοποιεί όλα τα πεδία του CartItem
    public CartItem(String dishName, int quantity, String description, double unitPrice, String extras, String category) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.description = description;
        this.unitPrice = unitPrice;
        this.extras = extras;
        this.category = category;
    }

    public String getDishName() { return dishName; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getExtras() { return extras; }

    // Υπολογίζει την συνολική τιμή (μονάδα * ποσότητα), με στρογγυλοποίηση σε 2 δεκαδικά
    public double getTotalPrice() { return Math.round(unitPrice * quantity * 100.0) / 100.0; }
    // Υπολογίζει την καθαρή τιμή χωρίς Φ.Π.Α.
    public double getNetPrice() { return Math.round((getTotalPrice() / (1 + VAT_RATE)) * 100.0) / 100.0; }
    // Υπολογίζει το ποσό Φ.Π.Α.
    public double getVat() { return Math.round((getTotalPrice() - getNetPrice()) * 100.0) / 100.0; }
    // Ορίζει νέα ποσότητα
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
