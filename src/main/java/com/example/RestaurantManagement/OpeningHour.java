package com.example.RestaurantManagement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Κλάση μοντέλου για ώρα λειτουργίας
public class OpeningHour {
    private final StringProperty day;
    private final StringProperty dineIn;

    public OpeningHour(String day, String dineIn) {
        this.day = new SimpleStringProperty(day);
        this.dineIn = new SimpleStringProperty(dineIn);
    }

    public String getDay() { return day.get(); }
    public StringProperty dayProperty() { return day; }
    public String getDineIn() { return dineIn.get(); }
    public StringProperty dineInProperty() { return dineIn; }
}
