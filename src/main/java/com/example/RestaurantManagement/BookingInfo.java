package com.example.RestaurantManagement;

// Κλάση για αποθήκευση στοιχείων κράτησης
public class BookingInfo {
    private int people;
    private String name;
    private String email;

    // Κατασκευαστής αντικειμένου κράτησης
    public BookingInfo(int people, String name, String email) {
        this.people = people;
        this.name = name;
        this.email = email;
    }

    public int getPeople() {
        return people;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
