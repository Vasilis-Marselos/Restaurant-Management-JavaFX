package com.example.RestaurantManagement;

import javafx.beans.property.SimpleStringProperty;

// Μοντέλο για μια γραμμή παραγγελίας στον πίνακα παραγγελιών
public class OrderRow {
    private final SimpleStringProperty orderId;
    private final SimpleStringProperty tableId;
    private final SimpleStringProperty status;
    private final SimpleStringProperty items;
    private final SimpleStringProperty date;

    // Constructor με όλα τα πεδία
    public OrderRow(String orderId, String tableId, String status, String items, String date) {
        this.orderId = new SimpleStringProperty(orderId);
        this.tableId = new SimpleStringProperty(tableId);
        this.status = new SimpleStringProperty(status);
        this.items = new SimpleStringProperty(items);
        this.date = new SimpleStringProperty(date);
    }

    // Getters για τα πεδία
    public String getOrderId() { return orderId.get(); }
    public String getTableId() { return tableId.get(); }
    public String getStatus() { return status.get(); }
    public String getItems() { return items.get(); }
    public String getDate() { return date.get(); }

    // Setters για status και items (αν αλλάξουν)
    public void setItems(String i) { items.set(i); }
    public void setStatus(String s) { status.set(s); }
}
