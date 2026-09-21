package com.example.RestaurantManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {

    // Δήλωση των στοιχείων του UI (labels, πίνακας, κ.ά.)
    @FXML
    private Label totalIncomeLabel, dayIncomeLabel, monthIncomeLabel, ordersCountLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<TopDish> topDishesTable;
    @FXML
    private TableColumn<TopDish, String> dishNameCol;
    @FXML
    private TableColumn<TopDish, Integer> dishCountCol;
    @FXML
    private Label pickedDayIncomeLabel;

    // Εσωτερική κλάση για τα πιο δημοφιλή πιάτα
    public static class TopDish {
        private final String dishName;
        private final Integer count;

        public TopDish(String dishName, Integer count) {
            this.dishName = dishName;
            this.count = count;
        }

        public String getDishName() {
            return dishName;
        }

        public Integer getCount() {
            return count;
        }
    }

    // Μέθοδος που εκτελείται κατά την αρχικοποίηση του UI
    @FXML public void initialize() {
        // Αντιστοίχιση των στηλών του πίνακα με τις ιδιότητες της κλάσης TopDish
        dishNameCol.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        // Μεταβλητές για υπολογισμούς
        Map<String, Integer> dishCounts = new HashMap<>();
        int orderCount = 0;
        double totalIncome = 0.0;
        double todayIncome = 0.0;
        double monthIncome = 0.0;
        Map<LocalDate, Double> dayIncomeMap = new HashMap<>();

        LocalDate today = LocalDate.now();
        YearMonth thisMonth = YearMonth.now();


        // Διαβάζει το αρχείο dish_sales.csv και γεμίζει το dishCounts
        try (BufferedReader br = new BufferedReader(new FileReader("orders/dish_sales.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("Dish Name")) continue;
                String[] values = line.split(",");
                if (values.length < 2) continue;
                String dish = values[0].replace("\"", "").trim();
                int count = Integer.parseInt(values[1].trim());
                dishCounts.put(dish, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Διαβάζει το αρχείο all_orders.csv και υπολογίζει έσοδα και αριθμό παραγγελιών
        try (BufferedReader br = new BufferedReader(new FileReader("orders/all_orders.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("Date")) continue;
                String[] values = line.split(",");
                LocalDate lineDate = null;
                try {
                    lineDate = LocalDate.parse(values[0].trim());
                } catch (Exception ignored) {
                }

                if (line.contains("ΣΥΝΟΛΟ")) {
                    orderCount++;
                    double orderTotal = 0.0;
                    if (values.length > 6) {
                        try {
                            orderTotal = Double.parseDouble(values[6].trim());
                        } catch (Exception ignored) {
                        }
                    }

                    totalIncome += orderTotal;

                    // Αντιστοιχεί την παραγγελία σε ημερομηνία
                    if (lineDate != null) {
                        if (lineDate.equals(today)) todayIncome += orderTotal;
                        if (YearMonth.from(lineDate).equals(thisMonth)) monthIncome += orderTotal;

                        dayIncomeMap.put(lineDate, dayIncomeMap.getOrDefault(lineDate, 0.0) + orderTotal);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Ταξινόμηση των πιάτων κατά ποσότητα και επιλογή των 5 πρώτων
        List<Map.Entry<String, Integer>> sortedTop = dishCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Εμφάνιση των 5 πιο δημοφιλών πιάτων στον πίνακα
        ObservableList<TopDish> top5Data = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : sortedTop) {
            top5Data.add(new TopDish(entry.getKey(), entry.getValue()));
        }
        topDishesTable.setItems(top5Data);


        // Ενημέρωση των labels με τα συνολικά έσοδα και παραγγελίες
        totalIncomeLabel.setText(String.format("%.2f €", totalIncome));
        dayIncomeLabel.setText(String.format("%.2f €", todayIncome));
        monthIncomeLabel.setText(String.format("%.2f €", monthIncome));
        ordersCountLabel.setText(String.valueOf(orderCount));


        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string) : null;
            }
        });
        datePicker.setValue(today);

        // Εμφανίζει το εισόδημα για την ημερομηνία που επιλέγεται
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            double income = dayIncomeMap.getOrDefault(newVal, 0.0);
            pickedDayIncomeLabel.setText(String.format("%.2f €", income));
        });

        pickedDayIncomeLabel.setText(String.format("%.2f €", todayIncome));
    }
}