package com.example.RestaurantManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Κλάση για διαχείριση αντικειμένων καλαθιού παραγγελίας
public class CartManager {

    // Στατική λίστα καλαθιού που παρακολουθείται από το JavaFX
    private static final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    // Επιστροφή λίστας καλαθιού
    public static ObservableList<CartItem> getCartItems() { return cartItems; }

    // Προσθήκη αντικειμένου στο καλάθι (αν υπάρχει ήδη, αυξάνει την ποσότητα)
    public static void addItemToCart(CartItem newItem) {
        for (CartItem existingItem : cartItems) {
            if (existingItem.getDishName().equals(newItem.getDishName())
                    && existingItem.getDescription().equals(newItem.getDescription())
                    && existingItem.getExtras().equals(newItem.getExtras())) {
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        cartItems.add(newItem);
    }

    // Αφαίρεση αντικειμένου από το καλάθι
    public static void removeItemFromCart(CartItem item) { cartItems.remove(item); }

    // Υπολογισμός συνολικού κόστους καλαθιού
    public static double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Υπολογισμός συνολικής ποσότητας προϊόντων στο καλάθι
    public static int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }
}