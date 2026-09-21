package com.example.RestaurantManagement;

import java.time.LocalDateTime;

public class SessionData {
    private static int selectedTableNum = -1;
    private static LocalDateTime selectedSlot;
    private static boolean activeOrder = false;

    public static void setSelectedTable(int tableNum, LocalDateTime slot) {
        selectedTableNum = tableNum;
        selectedSlot = slot;
        activeOrder = true;
    }

    public static int getSelectedTableNum() {
        return selectedTableNum;
    }

    public static LocalDateTime getSelectedSlot() {
        return selectedSlot;
    }

    public static boolean hasActiveOrder() {
        return activeOrder;
    }

    public static void clearSession() {
        selectedTableNum = -1;
        selectedSlot = null;
        activeOrder = false;
    }
}
