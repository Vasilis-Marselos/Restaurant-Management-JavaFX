package com.example.RestaurantManagement;

import java.util.List;

// Κλάση μοντέλου που αναπαριστά ένα πιάτο στο μενού
public class Dish {
    private String name;
    private String description;
    private String imagePath;
    private List<String> extraIngredients;
    private double price;
    private String category;

    // Constructor για αρχικοποίηση των πεδίων του πιάτου
    public Dish(String name, String description, String imagePath, List<String> extraIngredients, double price, String category) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.extraIngredients = extraIngredients;
        this.price = price;
        this.category = category;
    }

    // Getters για κάθε πεδίο — πρόσβαση στα δεδομένα του πιάτου
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public List<String> getExtraIngredients() { return extraIngredients; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}