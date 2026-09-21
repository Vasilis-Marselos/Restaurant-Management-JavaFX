package com.example.RestaurantManagement;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CartController {

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> dishNameColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, String> descriptionColumn;
    @FXML private TableColumn<CartItem, String> extrasColumn;
    @FXML private TableColumn<CartItem, String> categoryColumn;
    @FXML private TableColumn<CartItem, Double> netPriceColumn;
    @FXML private TableColumn<CartItem, Double> vatColumn;
    @FXML private TableColumn<CartItem, Double> totalPriceColumn;
    @FXML private Label totalPriceLabel;
    @FXML private Button removeSelectedButton;
    @FXML private Button undoRemoveButton;
    @FXML private Button confirmOrderButton;
    @FXML private Label cartItemCountLabel;
    @FXML private Button clearCartButton;
    @FXML private Label tableInfoLabel;

    // Στοίβα για undo λειτουργία
    private Stack<CartItem> removedItemsStack = new Stack<>();

    private int orderIdCounter = 1;

    public void initialize() {
        // Αρχικοποίηση Order ID από αρχείο
        readLastOrderId();
        setupTableColumns();
        // Γέμισμα του TableView με τα αντικείμενα του καλαθιού
        cartTable.setItems(CartManager.getCartItems());
        updateTableInfoLabel();

        // Listener για να ενημερώνει αυτόματα όταν αλλάζει το καλάθι
        CartManager.getCartItems().addListener((javafx.collections.ListChangeListener<CartItem>) change -> {
            updateTotalPrice();
            updateCartItemCountLabel();
            updateTableInfoLabel();
        });

        // Συνδέσεις κουμπιών με τα αντίστοιχα event handlers
        removeSelectedButton.setOnMouseClicked(this::removeSelectedItems);
        undoRemoveButton.setOnMouseClicked(this::undoRemoveItem);
        confirmOrderButton.setOnMouseClicked(this::confirmOrder);
        clearCartButton.setOnMouseClicked(this::clearCart);

        // Αρχική ενημέρωση συνολικού ποσού και πλήθους
        updateTotalPrice();
        updateCartItemCountLabel();
    }


    // Ενημερώνει το label με το τραπέζι και ώρα
    private void updateTableInfoLabel() {
        int tableNum = SessionData.getSelectedTableNum();
        LocalDateTime slot = SessionData.getSelectedSlot();

        if (slot != null) {
            String formattedTime = slot.toLocalTime().toString();
            tableInfoLabel.setText("Τραπέζι: " + tableNum + " | Ώρα: " + formattedTime);
        } else {
            tableInfoLabel.setText("Δεν έχει επιλεγεί τραπέζι.");
        }
    }


    // Ενημερώνει το label με το πλήθος ειδών καλαθιού
    private void updateCartItemCountLabel() {
        if (cartItemCountLabel != null) {
            cartItemCountLabel.setText(String.valueOf(CartManager.getCartItemCount()));
            animateCartItemCountLabel();
        }
    }


    // Animation μεγέθυνσης αριθμού ειδών για οπτική ειδοποίηση
    private void animateCartItemCountLabel() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), cartItemCountLabel);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.3);
        st.setToY(1.3);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    // Σύνδεση στηλών TableView με ιδιότητες CartItem
    private void setupTableColumns() {
        dishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        netPriceColumn.setCellValueFactory(new PropertyValueFactory<>("netPrice"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        extrasColumn.setCellValueFactory(new PropertyValueFactory<>("extras"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        cartTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Ενεργοποιεί wrap text στις στήλες
        applyTextWrapping(dishNameColumn);
        applyTextWrapping(descriptionColumn);
        applyTextWrapping(extrasColumn);
        applyTextWrapping(quantityColumn);
        applyTextWrapping(netPriceColumn);
        applyTextWrapping(vatColumn);
        applyTextWrapping(totalPriceColumn);
        applyTextWrapping(categoryColumn);
    }

    // Κάνει wrap τα κελιά στις στήλες
    private <T> void applyTextWrapping(TableColumn<CartItem, T> column) {
        column.setCellFactory(col -> new TableCell<CartItem, T>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
                setGraphic(text);
                setStyle("-fx-padding: 5px;");
            }
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? null : item.toString());
            }
        });
    }


    // Έλεγχος για διπλότυπη παραγγελία (ίδιο περιεχόμενο)
    private boolean isDuplicateOrder() {
        Path path = Paths.get("orders", "all_orders.csv");
        if (!Files.exists(path)) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            StringBuilder currentOrder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;

                if (line.startsWith("Order ID") || line.startsWith("ΣΥΝΟΛΟ")) {
                    if (currentOrder.toString().equals(getCurrentOrderAsString())) {
                        return true;
                    }
                    currentOrder.setLength(0);
                } else {
                    currentOrder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Επιστρέφει την τρέχουσα παραγγελία ως String
    private String getCurrentOrderAsString() {
        StringBuilder sb = new StringBuilder();
        for (CartItem item : cartTable.getItems()) {
            sb.append(item.getDishName()).append(",")
                    .append(item.getQuantity()).append(",")
                    .append(item.getDescription()).append(",")
                    .append(item.getTotalPrice()).append(",")
                    .append(item.getExtras()).append("\n");
        }
        return sb.toString();
    }

    // Διαγράφει το επιλεγμένο προϊόν
    private void removeSelectedItems(MouseEvent event) {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            removedItemsStack.push(selectedItem);
            CartManager.removeItemFromCart(selectedItem);
            cartTable.refresh();
            cartTable.getSelectionModel().clearSelection();
        }
    }

    // Εκκαθάριση καλαθιού με επιβεβαίωση
    private void clearCart(MouseEvent event) {
        if (cartTable.getItems().isEmpty()) {
            showAlert(AlertType.INFORMATION, "Άδειο Καλάθι", null, "Το καλάθι είναι ήδη άδειο.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Εκκαθάριση Καλαθιού");
        alert.setHeaderText("Είστε σίγουροι ότι θέλετε να αδειάσετε το καλάθι;");
        alert.setContentText("Όλα τα αντικείμενα θα διαγραφούν.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            CartManager.getCartItems().clear();
            removedItemsStack.clear();
            cartTable.refresh();
            updateTotalPrice();
            updateCartItemCountLabel();
            showAlert(AlertType.INFORMATION, "Επιτυχία", null, "Το καλάθι καθαρίστηκε.");
        }
    }

    // Επαναφορά τελευταίου διαγραμμένου είδους
    private void undoRemoveItem(MouseEvent event) {
        if (!removedItemsStack.isEmpty()) {
            CartItem lastRemovedItem = removedItemsStack.pop();
            CartManager.addItemToCart(lastRemovedItem);
            cartTable.refresh();
        }
    }

    // Επιβεβαίωση και αποθήκευση παραγγελίας
    private void confirmOrder(MouseEvent event) {
        if (cartTable.getItems().isEmpty()) {
            showAlert(AlertType.WARNING, "Άδειο Καλάθι", "Το καλάθι σας είναι άδειο", "Προσθέστε πιάτα πριν την επιβεβαίωση.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Επιβεβαίωση Παραγγελίας");
        alert.setHeaderText("Είστε σίγουροι ότι θέλετε να ολοκληρώσετε την παραγγελία σας;");
        alert.setContentText("Πατήστε 'OK' για επιβεβαίωση.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            int orderId = generateOrderId();
            boolean saved = saveOrderToCSV(orderId);
            if (saved) {
                exportOrderToPDF(orderId);
                updateDishSales();

                if (isDuplicateOrder()) {
                    showAlert(AlertType.INFORMATION, "Διπλή Παραγγελία", null, "Η ίδια παραγγελία υπάρχει ήδη.");
                    return;
                }

                cartTable.getItems().clear();
                removedItemsStack.clear();
                updateTotalPrice();
                showAlert(AlertType.INFORMATION, "Επιτυχία", "Η παραγγελία καταχωρήθηκε!", "Σας ευχαριστούμε.");
            } else {
                showAlert(AlertType.ERROR, "Σφάλμα", "Σφάλμα αποθήκευσης παραγγελίας", "Δοκιμάστε ξανά.");
            }
        }
    }


    private int generateOrderId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("orders/all_orders.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 1) {
                    try {
                        int id = Integer.parseInt(values[1]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }


    // Αποθηκεύει την παραγγελία σε CSV αρχείο
    private boolean saveOrderToCSV(int orderId) {
        try {
            Path path = Paths.get("orders", "all_orders.csv");
            boolean fileExists = Files.exists(path);
            FileWriter csvWriter = new FileWriter(path.toString(), true);

            if (!fileExists) {
                csvWriter.append("Date,Order ID,Table Number,Όνομα Πιάτου,Ποσότητα,Περιγραφή,Τιμή,Έξτρα,Status\n");
            }

            int tableNumber = SessionData.getSelectedTableNum();
            LocalDateTime bookingDateTime = SessionData.getSelectedSlot();

            String date = (bookingDateTime != null) ? bookingDateTime.toLocalDate().toString() : LocalDate.now().toString();

            for (CartItem item : cartTable.getItems()) {
                String safeExtras = item.getExtras().replace(",", ";");

                if (tableNumber > 0) {
                    csvWriter.append(String.format("%s,%d,%d,\"%s\",%d,\"%s\",%.2f,\"%s\",%s\n",
                            date,
                            orderId,
                            tableNumber,
                            item.getDishName(),
                            item.getQuantity(),
                            item.getDescription(),
                            item.getTotalPrice(),
                            safeExtras,
                            "Pending"));
                } else {
                    csvWriter.append(String.format("%s,%d,,\"%s\",%d,\"%s\",%.2f,\"%s\",%s\n",
                            date,
                            orderId,
                            item.getDishName(),
                            item.getQuantity(),
                            item.getDescription(),
                            item.getTotalPrice(),
                            safeExtras,
                            "Pending"));
                }
            }

            csvWriter.append(String.format("%s,%d,%s,ΣΥΝΟΛΟ,,,%s,,Pending\n",
                    date,
                    orderId,
                    tableNumber > 0 ? String.valueOf(tableNumber) : "",
                    String.format("%.2f", CartManager.calculateTotal())));

            csvWriter.flush();
            csvWriter.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateDishSales() {
        Path salesPath = Paths.get("orders", "dish_sales.csv");
        Map<String, Integer> salesMap = new HashMap<>();

        if (Files.exists(salesPath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(salesPath.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith("Dish Name")) continue;
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        salesMap.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (CartItem item : cartTable.getItems()) {
            if (!item.getCategory().equalsIgnoreCase("Food")) continue;
            salesMap.put(item.getDishName(), salesMap.getOrDefault(item.getDishName(), 0) + item.getQuantity());
        }

        try (FileWriter writer = new FileWriter(salesPath.toFile())) {
            writer.append("Dish Name,Sales Count\n");
            for (Map.Entry<String, Integer> entry : salesMap.entrySet()) {
                writer.append(String.format("%s,%d\n", entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readLastOrderId() {
        Path filePath = Paths.get("orders", "all_orders.csv");
        if (Files.exists(filePath)) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty() || line.startsWith("Order ID") || line.startsWith("ΣΥΝΟΛΟ")) continue;
                    String[] columns = line.split(",");
                    try {
                        int lastOrderId = Integer.parseInt(columns[0]);
                        orderIdCounter = lastOrderId + 1;
                    } catch (NumberFormatException ignored) {}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void updateTotalPrice() {
        totalPriceLabel.setText(String.format("Σύνολο: %.2f €", CartManager.calculateTotal()));
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/RestaurantManagement/CSS/Alert.css").toExternalForm());
        alert.showAndWait();
    }


    // Εξάγει την παραγγελία σε PDF με πλήρη μορφοποίηση
    private void exportOrderToPDF(int orderId) {
        try {
            Path receiptsDir = Paths.get("orders", "receipts");
            if (!Files.exists(receiptsDir)) {
                Files.createDirectories(receiptsDir);
            }

            Document document = new Document();
            String fileName = receiptsDir.resolve("order_receipt_" + orderId + ".pdf").toString();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            document.open();

            Font headerFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font titleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);
            Font smallFont = new Font(Font.HELVETICA, 10);

            Paragraph restaurantHeader = new Paragraph("MAISON DE GOÛT", headerFont);
            restaurantHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(restaurantHeader);

            Paragraph restaurantSubheader = new Paragraph("RESTAURANT", headerFont);
            restaurantSubheader.setAlignment(Element.ALIGN_CENTER);
            document.add(restaurantSubheader);

            document.add(Chunk.NEWLINE);

            Paragraph receiptFor = new Paragraph("RECEIPT FOR :", titleFont);
            document.add(receiptFor);

            int tableNum = SessionData.getSelectedTableNum();
            if (tableNum != -1) {
                document.add(new Paragraph("Table " + tableNum, normalFont));
            } else {
                document.add(new Paragraph("Customer", normalFont));
            }

            document.add(Chunk.NEWLINE);

            Paragraph dueDate = new Paragraph("DUE DATE :", titleFont);
            document.add(dueDate);

            LocalDateTime slot = SessionData.getSelectedSlot();
            String dueDateText;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm");

            if (slot != null) {
                dueDateText = slot.format(formatter);
            } else {
                dueDateText = LocalDateTime.now().format(formatter);
            }

            document.add(new Paragraph(dueDateText, normalFont));

            document.add(Chunk.NEWLINE);

            Paragraph restaurantInfo = new Paragraph("RESTAURANT INFO :", titleFont);
            document.add(restaurantInfo);

            document.add(new Paragraph("+123-456-7890", smallFont));
            document.add(new Paragraph("INFO.MAISON.DE.GOUT@GMAIL.COM", smallFont));
            document.add(new Paragraph("123 ANYWHERE ST, ANY CITY", smallFont));

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 2});

            PdfPCell cell;

            cell = new PdfPCell(new Phrase("DESCRIPTION", titleFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("QTY", titleFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PRICE", titleFont));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            for (CartItem item : cartTable.getItems()) {
                table.addCell(new Phrase(item.getDishName(), normalFont));
                table.addCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));
                table.addCell(new Phrase(String.format("%.2f", item.getTotalPrice()), normalFont));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            Paragraph total = new Paragraph();
            total.add(new Chunk("TOTAL : ", titleFont));
            total.add(new Chunk(String.format("%.2f", CartManager.calculateTotal()), normalFont));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(Chunk.NEWLINE);

            Paragraph thankYou = new Paragraph("THANK YOU!", titleFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();
            System.out.println("PDF receipt created: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // === Σκηνές Εναλλαγής ===
    @FXML private void BreakfastOrder(javafx.event.ActionEvent event) throws IOException { loadScene("Breakfast_Order.fxml", event); }
    @FXML private void MainCourseOrder(javafx.event.ActionEvent event) throws IOException { loadScene("MainCourse_Order.fxml", event); }
    @FXML private void AppetizersOrder(javafx.event.ActionEvent event) throws IOException { loadScene("Appetizers_Order.fxml", event); }
    @FXML private void BeveragesOrder(javafx.event.ActionEvent event) throws IOException { loadScene("Beverages_Order.fxml", event); }
    @FXML private void Cart(javafx.event.ActionEvent event) throws IOException { loadScene("cart.fxml", event); }
    @FXML private void HomePage(javafx.event.ActionEvent event) throws IOException { loadScene("HomePage.fxml", event); }

    private void loadScene(String fxmlFile, javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
