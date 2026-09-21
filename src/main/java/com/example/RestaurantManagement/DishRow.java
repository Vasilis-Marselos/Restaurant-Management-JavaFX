package com.example.RestaurantManagement;

import javafx.beans.property.SimpleStringProperty;

// Κλάση για την αναπαράσταση μιας γραμμής πιάτου σε TableView
public class DishRow {
    private final SimpleStringProperty dishName;
    private final SimpleStringProperty quantity;
    private final SimpleStringProperty price;

    // Κατασκευαστής που αρχικοποιεί τις ιδιότητες με τις τιμές που δίνεται
    public DishRow(String dishName, String quantity, String price) {
        this.dishName = new SimpleStringProperty(dishName);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
    }

    public String getDishName() {
        return dishName.get();
    }

    public String getQuantity() {
        return quantity.get();
    }

    public String getPrice() {
        return price.get();
    }
}
