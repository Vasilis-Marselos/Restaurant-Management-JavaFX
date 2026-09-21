package com.example.RestaurantManagement;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Cursor;
import javafx.scene.control.TableView;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class RestaurantController {

    // Slideshow components
    @FXML private ImageView slideshowView;
    @FXML private Button prevBtn, nextBtn, makeResBtn;
    @FXML private Pane slideshowPane;

    // Reservation components
    @FXML private Pane reservationPane;
    @FXML private ImageView backgroundImage;

    @FXML private Button table1, table2, table3, table4, table5,
            table6, table7, table8, table9, table10,
            table11, table12, table13, table14, table15,
            table16, table17, table18, table19, table20;

    @FXML private DatePicker viewDate;
    @FXML private ComboBox<String> viewTime;
    @FXML private Button controlPanelBtn;

    // Slideshow data
    private final List<Image> slides = new ArrayList<>();
    private Timeline slideTimeline;
    private int currentSlide = 0;

    // Reservation data
    private final Map<Integer, Button> tableButtons = new HashMap<>();
    private final BookingManager bookingManager = new BookingManager();

    @FXML
    public void initialize() {
        if (slideshowView != null) initializeSlideshow();
        if (reservationPane != null) initializeReservation();
    }

    private void initializeSlideshow() {
        // Slideshow initialization
        slides.add(new Image(getClass().getResource("image/RoomSketcher 3D Photo (1).jpg").toExternalForm()));
        slides.add(new Image(getClass().getResource("image/RoomSketcher 3D Photo.jpg").toExternalForm()));
        slides.add(new Image(getClass().getResource("image/RoomSketcher 3D Photo (2).jpg").toExternalForm()));

        slideshowView.setImage(slides.get(0));
        slideTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            currentSlide = (currentSlide + 1) % slides.size();
            slideshowView.setImage(slides.get(currentSlide));
        }));
        slideTimeline.setCycleCount(Timeline.INDEFINITE);
        slideTimeline.play();

        prevBtn.setOnAction(this::onPrevClicked);
        nextBtn.setOnAction(this::onNextClicked);
        makeResBtn.setOnAction(evt -> {
            try {
                loadScene("TableReservation.fxml", evt);
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Could not load reservation page");
            }
        });
    }

    // Slideshow navigation buttons
    @FXML private void onPrevClicked(ActionEvent e) {
        currentSlide = (currentSlide - 1 + slides.size()) % slides.size();
        slideshowView.setImage(slides.get(currentSlide));
        slideTimeline.playFromStart();
    }

    @FXML private void onNextClicked(ActionEvent e) {
        currentSlide = (currentSlide + 1) % slides.size();
        slideshowView.setImage(slides.get(currentSlide));
        slideTimeline.playFromStart();
    }

    // Map τραπέζια σε buttons
    private void initializeReservation() {
        tableButtons.put( 1, table1);  tableButtons.put( 2, table2);
        tableButtons.put( 3, table3);  tableButtons.put( 4, table4);
        tableButtons.put( 5, table5);  tableButtons.put( 6, table6);
        tableButtons.put( 7, table7);  tableButtons.put( 8, table8);
        tableButtons.put( 9, table9);  tableButtons.put(10,table10);
        tableButtons.put(11,table11);  tableButtons.put(12,table12);
        tableButtons.put(13,table13);  tableButtons.put(14,table14);
        tableButtons.put(15,table15);  tableButtons.put(16,table16);
        tableButtons.put(17,table17);  tableButtons.put(18,table18);
        tableButtons.put(19,table19);  tableButtons.put(20,table20);

        tableButtons.values().forEach(btn -> {
            btn.setOnAction(this::onTableClicked);
            btn.setCursor(Cursor.HAND);
        });

        viewDate.setValue(LocalDate.now());
        viewTime.getItems().setAll("17:00","18:00","19:00","20:00","21:00");
        viewTime.setValue(viewTime.getItems().get(0));

        viewDate.valueProperty().addListener((o,ov,nv)->showAvailability());
        viewTime.valueProperty().addListener((o,ov,nv)->showAvailability());
        showAvailability();

        backgroundImage.setImage(new Image(
                getClass().getResource("image/Intimate-Restaurant-Layout-3D.jpg").toExternalForm()
        ));

        controlPanelBtn.setOnAction(this::onControlPanelClicked);
    }

    // Ενημερώνει τη διαθεσιμότητα τραπεζιών για την επιλεγμένη ημερομηνία και ώρα
    @FXML
    private void showAvailability() {
        if (viewDate.getValue()==null || viewTime.getValue()==null) return;
        LocalDateTime dt = LocalDateTime.of(
                viewDate.getValue(),
                LocalTime.parse(viewTime.getValue())
        );
        tableButtons.forEach((num, btn) -> {
            boolean booked = bookingManager.isBooked(num, dt);
            btn.setDisable(booked);
            btn.setStyle(booked
                    ? "-fx-background-color:rgba(200,0,0,0.3);"
                    : "-fx-background-color:transparent;");
        });
    }

    // Διαχείριση click σε τραπέζι
    @FXML
    private void onTableClicked(ActionEvent e) {
        Button btn = (Button)e.getSource();
        int tableNum = Integer.parseInt(btn.getId().replace("table",""));
        LocalDateTime slot = LocalDateTime.of(
                viewDate.getValue(),
                LocalTime.parse(viewTime.getValue())
        );

        if (slot.toLocalDate().isBefore(LocalDate.now())) {
            showAlert("Cannot manage past date.");
            return;
        }

        SessionData.setSelectedTable(tableNum, slot);

        if (bookingManager.isBooked(tableNum, slot)) {
            openManagePopup(tableNum, slot);
        } else {
            openBookingPopup(tableNum, slot);
        }
    }
    // Δημιουργεί νέο booking popup για εισαγωγή κράτησης από χρήστη
    private void openBookingPopup(int tableNum, LocalDateTime slot) {
        String tableName = "Τραπέζι " + tableNum;

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Νέα Κράτηση για " + tableName);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        g.add(new Label("Ημ/νία & Ώρα:"), 0, 0);
        g.add(new Label(slot.toString()), 1, 0);

        TextField pplField = new TextField();
        pplField.setPromptText("Άτομα");
        g.add(new Label("Άτομα:"), 0, 1);
        g.add(pplField, 1, 1);

        TextField nameField = new TextField();
        nameField.setPromptText("Ονοματεπώνυμο");
        g.add(new Label("Όνομα:"), 0, 2);
        g.add(nameField, 1, 2);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        g.add(new Label("Email:"), 0, 3);
        g.add(emailField, 1, 3);

        dlg.getDialogPane().setContent(g);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    int ppl = Integer.parseInt(pplField.getText().trim());
                    int cap = getTableCapacity(tableName);

                    if (ppl < 1 || ppl > cap) {
                        showAlert("Εισάγετε 1–" + cap + " άτομα.");
                        return;
                    }

                    if (bookingManager.isBooked(tableNum, slot)) {
                        showAlert(tableName + " είναι ήδη κλεισμένο για " + slot + ".");
                        return;
                    }

                    String name = nameField.getText().trim();
                    String email = emailField.getText().trim();

                    if (name.isEmpty() || email.isEmpty()) {
                        showAlert("Συμπληρώστε όνομα και email.");
                        return;
                    }

                    bookingManager.addBooking(tableNum, slot, ppl, name, email,true);

                    showInfo("Κράτηση: " + tableName + " για " + ppl + " άτομα.\nΌνομα: " + name + "\nEmail: " + email);
                    showAvailability();

                } catch (NumberFormatException ex) {
                    showAlert("Μη έγκυρος αριθμός.");
                }
            }
        });
    }




    // Popup για διαχείριση υπάρχουσας κράτησης (τροποποίηση/ακύρωση)
    private void openManagePopup(int tableNum, LocalDateTime orig) {
        String tableName = "Τραπέζι " + tableNum;

        BookingInfo info = bookingManager.getAllBookings()
                .getOrDefault(tableNum, Collections.emptyMap())
                .get(orig);
        int existing = (info != null) ? info.getPeople() : 0;

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Manage " + tableName);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.LEFT);
        ButtonType modify = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(modify, cancel, ButtonType.CANCEL);
        dlg.setContentText(existing + " people @ " + orig);

        dlg.showAndWait().ifPresent(resp -> {
            if (resp == cancel) {
                bookingManager.cancelBooking(tableNum, orig);
                showInfo("Cancelled.");
                showAvailability();
            } else if (resp == modify) {
                Dialog<ButtonType> md = new Dialog<>();
                md.setTitle("Modify " + tableName);
                GridPane g2 = new GridPane();
                g2.setHgap(10);
                g2.setVgap(10);
                g2.setPadding(new Insets(20));

                TextField f2 = new TextField(String.valueOf(existing));
                g2.add(new Label("People:"), 0, 0);
                g2.add(f2, 1, 0);

                md.getDialogPane().setContent(g2);
                md.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                md.showAndWait().ifPresent(r2 -> {
                    if (r2 == ButtonType.OK) {
                        try {
                            int np = Integer.parseInt(f2.getText().trim());
                            int cap = getTableCapacity(tableName);
                            if (np < 1 || np > cap) {
                                showAlert("Enter 1–" + cap);
                                return;
                            }

                            showInfo("Modified.");
                            showAvailability();
                        } catch (NumberFormatException ex) {
                            showAlert("Invalid number.");
                        }
                    }
                });
            }
        });
    }


    // Διαχείριση κουμπιού για είσοδο στο Control Panel (login popup)
    @FXML private void onControlPanelClicked(ActionEvent e) {

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Admin Login");


        String css = getClass().getResource("CSS/styles.css").toExternalForm();
        dlg.getDialogPane().getStylesheets().add(css);
        dlg.getDialogPane().getStyleClass().add("login-dialog");

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        TextField u = new TextField();
        PasswordField p = new PasswordField();
        g.add(new Label("User:"), 0, 0);
        g.add(u, 1, 0);
        g.add(new Label("Pass:"), 0, 1);
        g.add(p, 1, 1);
        dlg.getDialogPane().setContent(g);

        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                if (u.getText().equals("admin") && p.getText().equals("2005")) {
                    openControlPanel();
                } else {
                    showAlert("Invalid credentials.");
                }
            }
        });
    }

    // Ανοίγει το Control Panel για εμφάνιση και διαχείριση κρατήσεων
    private void openControlPanel() {
        String css = getClass().getResource("CSS/styles.css").toExternalForm();

        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Control Panel");

        TableView<BookingRow> tv = new TableView<>();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BookingRow, String> c1 = new TableColumn<>("Table");
        c1.setCellValueFactory(new PropertyValueFactory<>("tableNum"));

        TableColumn<BookingRow, String> c2 = new TableColumn<>("DateTime");
        c2.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        TableColumn<BookingRow, Integer> c3 = new TableColumn<>("People");
        c3.setCellValueFactory(new PropertyValueFactory<>("people"));

        TableColumn<BookingRow, String> c4 = new TableColumn<>("Email");
        c4.setCellValueFactory(new PropertyValueFactory<>("email"));

        tv.getColumns().addAll(c1, c2, c3, c4);

        ObservableList<BookingRow> rows = FXCollections.observableArrayList();
        bookingManager.getAllBookings().forEach((tbl, map) ->
                map.forEach((dt, info) ->
                        rows.add(new BookingRow(tbl, dt.toString(), info.getPeople(), info.getEmail()))
                )
        );
        tv.setItems(rows);

        Button add = new Button("Add"),
                mod = new Button("Modify"),
                del = new Button("Delete"),
                order = new Button("Make Order"),
                ordersBtn = new Button("Show Orders");
        Button dashboardBtn = new Button("Dashboard");
        dashboardBtn.getStyleClass().add("button");
        dashboardBtn.setOnAction(e -> openDashboard());

        add.getStyleClass().add("button");
        mod.getStyleClass().add("button");
        del.getStyleClass().add("button");
        order.getStyleClass().add("button");
        ordersBtn.getStyleClass().add("button");

        add.setOnAction(e -> openAddInControl(rows, tv));
        mod.setOnAction(e -> openModifyInControl(rows, tv));

        del.setOnAction(e -> {
            BookingRow sel = tv.getSelectionModel().getSelectedItem();
            if (sel != null) {
                BookingInfo info = bookingManager.getAllBookings()
                        .get(sel.getTableNum())
                        .get(LocalDateTime.parse(sel.getDateTime()));

                bookingManager.cancelBooking(sel.getTableNum(), LocalDateTime.parse(sel.getDateTime()));
                rows.remove(sel);


                String subject = "Ακύρωση Κράτησης - Maison De Gout";
                String body = "Αγαπητέ/ή " + info.getName() + ",\n\n" +
                        "Η κράτησή σας στο Maison De Gout για:\n\n" +
                        "📍 Τραπέζι: " + sel.getTableNum() + "\n" +
                        "📅 Ημερομηνία & Ώρα: " + sel.getDateTime() + "\n" +
                        "👥 Άτομα: " + info.getPeople() + "\n\n" +
                        "ακυρώθηκε όπως ζητήσατε.\n\n" +
                        "Ελπίζουμε να σας υποδεχθούμε σύντομα σε κάποια άλλη ευκαιρία!\n\n" +
                        "Με εκτίμηση,\nMaison De Gout";

                bookingManager.sendCustomEmail(info.getEmail(), subject, body);
            }
        });

        order.setOnAction(e -> {
            BookingRow selected = tv.getSelectionModel().getSelectedItem();
            if (selected != null) {
                System.out.println("Selected table: " + selected.getTableNum() + " " + selected.getDateTime());

                SessionData.setSelectedTable(selected.getTableNum(), LocalDateTime.parse(selected.getDateTime()));

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Breakfast_Order.fxml"));
                    if (loader.getLocation() == null) {
                        System.out.println("FXML not found!");
                        return;
                    }
                    Parent root = loader.load();
                    Stage menuStage = new Stage();
                    menuStage.setTitle("Breakfast Menu - Τραπέζι: " + selected.getTableNum());
                    menuStage.setScene(new Scene(root));
                    menuStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Σφάλμα");
                    errorAlert.setHeaderText("Αδυναμία φόρτωσης menu");
                    errorAlert.setContentText("Παρουσιάστηκε πρόβλημα κατά το άνοιγμα του μενού.");
                    errorAlert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Καμία επιλογή");
                alert.setHeaderText("Δεν έχει επιλεγεί τραπέζι");
                alert.setContentText("Παρακαλώ επίλεξε πρώτα τραπέζι για την παραγγελία.");
                alert.showAndWait();
            }
        });

        ordersBtn.setOnAction(e -> openOrdersPanel());

        VBox toolbar = new VBox(tv, new HBox(10, add, mod, del, order, ordersBtn, dashboardBtn));

        VBox vb = new VBox(toolbar, tv);
        vb.setSpacing(10);
        vb.setPadding(new Insets(10));
        VBox.setVgrow(tv, Priority.ALWAYS);

        Scene scene = new Scene(vb, 700, 400);
        scene.getStylesheets().add(css);
        scene.getRoot().getStyleClass().add("control-panel-scene");

        st.setScene(scene);
        st.showAndWait();
    }

    // Ανοίγει το Dashboard παράθυρο
    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Διαβάζει όλες τις παραγγελίες από CSV αρχείο και τις φορτώνει σε λίστα
    private List<OrderRow> loadOrdersFromCSV() {
        List<OrderRow> ordersList = new ArrayList<>();
        Map<String, OrderRow> ordersMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("orders/all_orders.csv"))) {
            String header = br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length >= 9) {
                    String date = values[0];
                    String orderId = values[1];
                    String tableId = values[2];
                    String dishName = values[3];
                    String quantity = values[4];
                    String status = values[8];

                    String uniqueKey = date + "_" + orderId;

                    // Αν δεν υπάρχει ήδη το order, το φτιάχνουμε
                    if (!ordersMap.containsKey(uniqueKey)) {
                        ordersMap.put(uniqueKey, new OrderRow(orderId, tableId, status, dishName + " x" + quantity, date));
                    } else {
                        // Αν υπάρχει, προσθέτουμε το νέο πιάτο στα items
                        OrderRow existingOrder = ordersMap.get(uniqueKey);
                        String currentItems = existingOrder.getItems();
                        existingOrder.setItems(currentItems + ", " + dishName + " x" + quantity);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ordersList.addAll(ordersMap.values());
        return ordersList;
    }

    // Διαβάζει τα πιάτα μιας συγκεκριμένης παραγγελίας από CSV αρχείο
    private List<DishRow> loadDishesForOrder(String orderId) {
        List<DishRow> dishes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("orders/all_orders.csv"))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length >= 7) {
                    String csvOrderId = values[1];

                    if (orderId.equals(csvOrderId) && !values[3].equalsIgnoreCase("ΣΥΝΟΛΟ")) {
                        String dishName = values[3].replace("\"", "");
                        String quantity = values[4];
                        String price = values[6];
                        dishes.add(new DishRow(dishName, quantity, price));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dishes;
    }






    // Εμφανίζει το παράθυρο με όλες τις παραγγελίες και τις επιλογές φιλτραρίσματος, αλλαγής κατάστασης και εμφάνισης ειδών.
    private void openOrdersPanel() {
        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("Παραγγελίες");

        TableView<OrderRow> ordersTable = new TableView<>();
        ordersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<OrderRow, String> col1 = new TableColumn<>("Order ID");
        col1.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<OrderRow, String> col2 = new TableColumn<>("Table Number");
        col2.setCellValueFactory(new PropertyValueFactory<>("tableId"));

        TableColumn<OrderRow, String> col3 = new TableColumn<>("Κατάσταση");
        col3.setCellValueFactory(new PropertyValueFactory<>("status"));

        ordersTable.getColumns().addAll(col1, col2, col3);

        ObservableList<OrderRow> ordersList = FXCollections.observableArrayList(loadOrdersFromCSV());

        FilteredList<OrderRow> filteredOrders = new FilteredList<>(ordersList, p -> true);
        ordersTable.setItems(filteredOrders);

        ordersTable.setRowFactory(tv -> {
            TableRow<OrderRow> row = new TableRow<>();

            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                row.getStyleClass().removeAll("delivered-row", "pending-row");
                if (newItem != null) {
                    if ("Delivered".equals(newItem.getStatus())) {
                        row.getStyleClass().add("delivered-row");
                    } else {
                        row.getStyleClass().add("pending-row");
                    }
                }
            });

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    OrderRow selectedOrder = row.getItem();
                    showOrderItemsPopup(selectedOrder);
                }
            });

            return row;
        });

        Label totalOrdersLabel = new Label("Σύνολο παραγγελιών: " + ordersList.size());
        totalOrdersLabel.getStyleClass().add("orders-label");

        Label statusCountsLabel = new Label();
        statusCountsLabel.getStyleClass().add("orders-label");

        Label orderItemsLabel = new Label("Επιλέξτε παραγγελία για να δείτε τι έχει.");
        orderItemsLabel.setWrapText(true);
        orderItemsLabel.getStyleClass().add("orders-label");

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση Order ID ή Table...");
        searchField.getStyleClass().add("search-field");

        ChoiceBox<String> statusFilterBox = new ChoiceBox<>();
        statusFilterBox.getItems().addAll("Όλες", "Pending", "Delivered");
        statusFilterBox.setValue("Όλες");

        statusFilterBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            applyFilters(filteredOrders, searchField.getText(), newVal);
            updateStatusCounts(statusCountsLabel, ordersList);
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            applyFilters(filteredOrders, newVal, statusFilterBox.getValue());
            updateStatusCounts(statusCountsLabel, ordersList);
        });

        Button changeStatusBtn = new Button("Παράδοση (Delivered)");
        changeStatusBtn.getStyleClass().add("golden-button");

        changeStatusBtn.setOnAction(e -> {
            OrderRow selected = ordersTable.getSelectionModel().getSelectedItem();
            if (selected != null && !"Delivered".equals(selected.getStatus())) {
                selected.setStatus("Delivered");
                ordersTable.refresh();
                saveOrdersToCSV(ordersList);
                updateStatusCounts(statusCountsLabel, ordersList);
            }
        });

        updateStatusCounts(statusCountsLabel, ordersList);

        HBox topBar = new HBox(10, totalOrdersLabel, searchField, statusFilterBox);
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox vb = new VBox(10, topBar, statusCountsLabel, ordersTable, orderItemsLabel, changeStatusBtn);
        vb.setPadding(new Insets(10));
        VBox.setVgrow(ordersTable, Priority.ALWAYS);

        Scene scene = new Scene(vb, 650, 550);
        String css = getClass().getResource("CSS/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        st.setScene(scene);
        st.showAndWait();
    }

    // Άνοιγμα παραθύρου για επεξεργασία κράτησης (αλλαγή αριθμού ατόμων).
    private void openModifyInControl(ObservableList<BookingRow> rows, TableView<BookingRow> tv) {
        BookingRow sel = tv.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        LocalDateTime orig = LocalDateTime.parse(sel.getDateTime());
        if (orig.toLocalDate().isBefore(LocalDate.now())) {
            showAlert("Cannot modify past.");
            return;
        }

        BookingInfo info = bookingManager.getAllBookings()
                .get(sel.getTableNum())
                .get(orig);

        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Modify Booking (People Only)");
        GridPane gp = new GridPane();
        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(20));

        TextField pplF = new TextField(String.valueOf(sel.getPeople()));

        gp.add(new Label("People:"), 0, 0); gp.add(pplF, 1, 0);

        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    int np = Integer.parseInt(pplF.getText().trim());
                    String tableName = "Table " + sel.getTableNum();
                    int cap = getTableCapacity(tableName);
                    if (np < 1 || np > cap) {
                        showAlert("Enter 1–" + cap);
                        return;
                    }

                    int tableNum = sel.getTableNum();

                    int oldPeople = info.getPeople();

                    bookingManager.updateBooking(tableNum, orig, orig, np, info.getName(), info.getEmail());

                    sel.setPeople(np);
                    tv.refresh();

                    String subject = "Η Κράτησή σας Ανανεώθηκε - Maison De Gout";
                    String body = "Αγαπητέ/ή " + info.getName() + ",\n\n" +
                            "Σας ενημερώνουμε ότι η κράτησή σας στο Maison De Gout τροποποιήθηκε με επιτυχία!\n\n" +
                            "Νέες λεπτομέρειες:\n" +
                            "📍 Τραπέζι: " + tableNum + "\n" +
                            "📅 Ημερομηνία & Ώρα: " + orig + "\n" +
                            "👥 Άτομα: " + np + "\n\n" +
                            "Ανυπομονούμε να σας υποδεχθούμε και να σας προσφέρουμε μια ξεχωριστή γαστρονομική εμπειρία.\n" +
                            "Σας ευχαριστούμε που μας επιλέξατε!\n\n" +
                            "Με εκτίμηση,\nMaison De Gout";


                    bookingManager.sendCustomEmail(info.getEmail(), subject, body);

                } catch (NumberFormatException ex) {
                    showAlert("Invalid number.");
                }
            }
        });
    }

    // Ενημερώνει την ένδειξη πλήθους παραγγελιών ανά κατάσταση (Pending / Delivered).
    private void updateStatusCounts(Label label, ObservableList<OrderRow> orders) {
        long deliveredCount = orders.stream().filter(o -> "Delivered".equals(o.getStatus())).count();
        long pendingCount = orders.stream().filter(o -> !"Delivered".equals(o.getStatus())).count();
        label.setText("Pending: " + pendingCount + " | Delivered: " + deliveredCount);
    }

    // Εφαρμόζει φίλτρα στην λίστα παραγγελιών, με βάση το Order ID / Table και κατάσταση.
    private void applyFilters(FilteredList<OrderRow> filteredOrders, String searchText, String statusFilter) {
        filteredOrders.setPredicate(order -> {
            boolean matchesSearch = (searchText == null || searchText.isEmpty()) ||
                    order.getOrderId().toLowerCase().contains(searchText.toLowerCase()) ||
                    order.getTableId().toLowerCase().contains(searchText.toLowerCase());

            if ("Όλες".equals(statusFilter)) return matchesSearch;
            return matchesSearch && order.getStatus().equals(statusFilter);
        });
    }

    // Εμφανίζει popup παράθυρο με τα πιάτα μιας παραγγελίας.
    private void showOrderItemsPopup(OrderRow order) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Παραγγελία Τραπεζιού " + order.getTableId());

        Label orderIdLabel = new Label("Order ID: " + order.getOrderId());
        Label statusLabel = new Label("Κατάσταση: " + order.getStatus());

        TableView<DishRow> dishesTable = new TableView<>();
        dishesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<DishRow, String> dishCol = new TableColumn<>("Πιάτο");
        dishCol.setCellValueFactory(new PropertyValueFactory<>("dishName"));

        TableColumn<DishRow, String> quantityCol = new TableColumn<>("Ποσότητα");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<DishRow, String> priceCol = new TableColumn<>("Τιμή");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        dishesTable.getColumns().addAll(dishCol, quantityCol, priceCol);

        ObservableList<DishRow> dishRows = FXCollections.observableArrayList(loadDishesForOrder(order.getOrderId()));
        dishesTable.setItems(dishRows);


        double total = dishRows.stream()
                .mapToDouble(d -> Double.parseDouble(d.getPrice()))
                .sum();

        Label totalLabel = new Label(String.format("Σύνολο: %.2f€", total));

        Button closeBtn = new Button("Κλείσιμο");
        closeBtn.getStyleClass().add("golden-button");
        closeBtn.setOnAction(e -> dialog.close());

        HBox buttonsBox = new HBox(closeBtn);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox vb = new VBox(15, orderIdLabel, statusLabel, dishesTable, totalLabel, buttonsBox);
        vb.setPadding(new Insets(20));
        vb.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(dishesTable, Priority.ALWAYS);

        Scene scene = new Scene(vb, 600, 500);
        String css = getClass().getResource("CSS/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Αποθηκεύει τις αλλαγές των παραγγελιών στο CSV αρχείο.
    private void saveOrdersToCSV(List<OrderRow> updatedOrders) {
        String csvFile = "orders/all_orders.csv";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String header = br.readLine();
            lines.add(header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length >= 9 && !values[1].isEmpty() && !values[2].isEmpty()) {
                    String orderId = values[1];
                    String tableId = values[2];

                    for (OrderRow updatedOrder : updatedOrders) {
                        if (orderId.equals(updatedOrder.getOrderId())
                                && tableId.equals(updatedOrder.getTableId())) {

                            // Πάρε την ημερομηνία της παραγγελίας από το OrderRow
                            String bookingDate = updatedOrder.getDate();

                            // Αν είναι άδειο, fallback στο σημερινό
                            if (bookingDate == null || bookingDate.isEmpty()) {
                                bookingDate = LocalDate.now().toString();
                            }

                            // Ενημέρωσε το πεδίο date (στήλη 0) και status (στήλη 8)
                            values[0] = bookingDate;
                            values[8] = updatedOrder.getStatus();
                            break;
                        }
                    }
                    line = String.join(",", values);
                }
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (String l : lines) {
                bw.write(l + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Άνοιγμα παραθύρου προσθήκης νέας κράτησης.
    private void openAddInControl(ObservableList<BookingRow> rows, TableView<BookingRow> tv) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Booking");

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20));

        ComboBox<Integer> tableCombo = new ComboBox<>();
        for (int i = 1; i <= 20; i++) tableCombo.getItems().add(i);
        tableCombo.setValue(1);

        DatePicker dp = new DatePicker();
        ComboBox<String> tm = new ComboBox<>();
        tm.getItems().setAll("17:00", "18:00", "19:00", "20:00", "21:00");
        tm.setValue(tm.getItems().get(0));

        TextField pplF = new TextField();
        TextField nameF = new TextField();
        TextField emailF = new TextField();

        gp.add(new Label("Table:"), 0, 0);
        gp.add(tableCombo, 1, 0);
        gp.add(new Label("Date:"), 0, 1);
        gp.add(dp, 1, 1);
        gp.add(new Label("Time:"), 0, 2);
        gp.add(tm, 1, 2);
        gp.add(new Label("People:"), 0, 3);
        gp.add(pplF, 1, 3);
        gp.add(new Label("Name:"), 0, 4);
        gp.add(nameF, 1, 4);
        gp.add(new Label("Email:"), 0, 5);
        gp.add(emailF, 1, 5);

        dlg.getDialogPane().setContent(gp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {
                    LocalDate d = dp.getValue();
                    if (d == null || d.isBefore(LocalDate.now())) {
                        showAlert("Δεν μπορείτε να κάνετε κράτηση για περασμένη ημερομηνία.");
                        return;
                    }
                    LocalDateTime dt = LocalDateTime.of(d, LocalTime.parse(tm.getValue()));
                    int ppl = Integer.parseInt(pplF.getText().trim());
                    String name = nameF.getText().trim();
                    String email = emailF.getText().trim();
                    int tableNum = tableCombo.getValue();
                    String tableName = "Τραπέζι " + tableNum;
                    int cap = getTableCapacity(tableName);

                    if (ppl < 1 || ppl > cap) {
                        showAlert("Εισάγετε 1–" + cap + ".");
                        return;
                    }

                    if (bookingManager.isBooked(tableNum, dt)) {
                        showAlert(tableName + " είναι ήδη κλεισμένο για " + dt + ".");
                        return;
                    }

                    bookingManager.addBooking(tableNum, dt, ppl, name, email,true);

                    rows.add(new BookingRow(tableNum, dt.toString(), ppl, email));

                } catch (NumberFormatException ex) {
                    showAlert("Invalid number format.");
                }
            }
        });
    }

    // Αντιπροσωπεύει μία κράτηση σε TableView.
    public class BookingRow {
        private final int tableNum;
        private final String dateTime;
        private int people; // αφαιρούμε το final
        private final String email;

        public BookingRow(int tableNum, String dateTime, int people, String email) {
            this.tableNum = tableNum;
            this.dateTime = dateTime;
            this.people = people;
            this.email = email;
        }

        public int getTableNum() { return tableNum; }
        public String getDateTime() { return dateTime; }
        public int getPeople() { return people; }
        public String getEmail() { return email; }

        public void setPeople(int people) {
            this.people = people;
        }
    }

    // Επιστρέφει τη χωρητικότητα του τραπεζιού με βάση το όνομα του.
    private int getTableCapacity(String tableName) {
        try {
            int num = Integer.parseInt(tableName.replaceAll("\\D",""));
            switch(num) {
                case 1: return 5;
                case 2: case 3: return 6;
                case 4: return 4;
                case 5: case 6: case 7: case 10: case 11: case 13: case 14: return 2;
                case 8: case 9: case 12: return 4;
                case 15: case 16: case 17: case 18: return 6;
                case 19: return 10;
                case 20: return 12;
                default: return 6;
            }
        } catch(Exception e) {
            return 6;
        }
    }

    // Εμφανίζει παράθυρο σφάλματος με μήνυμα.
    private void showAlert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    // Εμφανίζει παράθυρο πληροφοριών με μήνυμα.
    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }


    @FXML public void breakfast_menu1(ActionEvent ev) {
        try { loadScene("breakfast_menu1.fxml", ev); }
        catch(IOException e) { e.printStackTrace(); showAlert("Could not load breakfast menu"); }
    }
    @FXML public void HomePage      (ActionEvent ev) {
        try { loadScene("HomePage.fxml", ev); }
        catch(IOException e) { e.printStackTrace(); showAlert("Could not load home page"); }
    }
    @FXML public void OurStory      (ActionEvent ev) {
        try { loadScene("OurStory.fxml", ev); }
        catch(IOException e) { e.printStackTrace(); showAlert("Could not load story page"); }
    }
    @FXML public void Contact       (ActionEvent ev) {
        try { loadScene("Contact.fxml", ev); }
        catch(IOException e) { e.printStackTrace(); showAlert("Could not load contact page"); }
    }
    @FXML public void Reservation   (ActionEvent ev) {
        try { loadScene("RestaurantLayout.fxml", ev); }
        catch(IOException e) { e.printStackTrace(); showAlert("Could not load reservation page"); }
    }
    @FXML public void TableReservation(ActionEvent ev) {
        try { loadScene("TableReservation.fxml", ev); }
        catch(IOException e) { e.printStackTrace(); showAlert("Could not load table reservation page"); }
    }

    private void loadScene(String fxmlFile, ActionEvent ev) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}