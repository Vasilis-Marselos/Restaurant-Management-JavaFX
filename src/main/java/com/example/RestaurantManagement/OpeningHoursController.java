package com.example.RestaurantManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class OpeningHoursController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Σύνδεση με στοιχεία του FXML
    @FXML private TableView<OpeningHour> tableView;
    @FXML private TableColumn<OpeningHour, String> dayColumn;
    @FXML private TableColumn<OpeningHour, String> dineInColumn;
    @FXML private TextField nameField, emailField, phoneField, subjectField, messageField;
    @FXML private Button sendButton;

    // Εκκίνηση πινάκα με ώρες λειτουργίας και ρύθμιση εμφάνισης
    public void initialize() {
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        dineInColumn.setCellValueFactory(new PropertyValueFactory<>("dineIn"));

        ObservableList<OpeningHour> data = FXCollections.observableArrayList(
                new OpeningHour("Monday – Saturday", "17:00 – 00:30"),
                new OpeningHour("Sunday", "17:00 – 00:00"),
                new OpeningHour("Sunday (Lunch)", "13:00 – 00:00"),
                new OpeningHour("Sunday (Takeaway)", "14:00 – 00:00")
        );

        tableView.setItems(data);

        // Ορισμός ύψους γραμμών και απενεργοποίηση λειτουργιών του πίνακα
        double rowHeight = 50;
        tableView.setFixedCellSize(rowHeight);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(data.size()).add(30));

        tableView.setSelectionModel(null);
        tableView.setEditable(false);
        dayColumn.setSortable(false);
        dineInColumn.setSortable(false);
        dayColumn.setReorderable(false);
        dineInColumn.setReorderable(false);
    }

    // Αποστολή email στον πελάτη και το εστιατόριο μέσω SMTP
    @FXML
    private void sendEmail(ActionEvent event) {
        if (System.getenv("SMTP_USERNAME") == null || System.getenv("SMTP_PASSWORD") == null
                || System.getenv("SMTP_USERNAME").isBlank() || System.getenv("SMTP_PASSWORD").isBlank()) {
            new Alert(Alert.AlertType.INFORMATION, "Email is disabled. Configure SMTP_USERNAME and SMTP_PASSWORD.").showAndWait();
            return;
        }
        String customerEmail = emailField.getText();
        String restaurantEmail = System.getenv("SMTP_USERNAME");
        String from = System.getenv("SMTP_USERNAME");
        String host = "smtp.gmail.com";

        // Ρυθμίσεις για SMTP
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Στοιχεία σύνδεσης Gmail
        String username = System.getenv("SMTP_USERNAME");
        String password = System.getenv("SMTP_PASSWORD");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Περιεχόμενο email με στοιχεία πελάτη
            String emailContent = "<h2>Στοιχεία Πελάτη</h2>"
                    + "<p><strong>Όνομα:</strong> " + nameField.getText() + "<br>"
                    + "<strong>Email:</strong> " + emailField.getText() + "<br>"
                    + "<strong>Τηλέφωνο:</strong> " + phoneField.getText() + "<br>"
                    + "<strong>Θέμα:</strong> " + subjectField.getText() + "<br>"
                    + "<strong>Μήνυμα:</strong> " + messageField.getText() + "</p>";

            String subjectText = subjectField.getText();

            // Email απάντησης προς πελάτη
            MimeMessage messageToCustomer = new MimeMessage(session);
            messageToCustomer.setFrom(new InternetAddress(from, "Maison de Gout"));
            messageToCustomer.addRecipient(Message.RecipientType.TO, new InternetAddress(customerEmail));
            messageToCustomer.setSubject("Ευχαριστούμε για την επικοινωνία σας: " + subjectText);
            messageToCustomer.setContent(
                    "<p>Ευχαριστούμε πολύ για τη στήριξή σας!</p>"
                            + "<p>Θα σας απαντήσουμε εντός των επόμενων ημερών.</p>"
                            + "<hr>"
                            + "<h3>Αντίγραφο του μηνύματός σας:</h3>"
                            + emailContent,
                    "text/html; charset=utf-8"
            );

            Transport.send(messageToCustomer);

            // Email ειδοποίησης προς το εστιατόριο
            MimeMessage messageToRestaurant = new MimeMessage(session);
            messageToRestaurant.setFrom(new InternetAddress(from, "Maison de Gout Website"));
            messageToRestaurant.addRecipient(Message.RecipientType.TO, new InternetAddress(restaurantEmail));
            messageToRestaurant.setSubject("Νέο μήνυμα από: " + nameField.getText()
                    + " <" + customerEmail + "> - Θέμα: " + subjectText);
            messageToRestaurant.setReplyTo(new Address[]{new InternetAddress(customerEmail)});
            messageToRestaurant.setContent(emailContent, "text/html; charset=utf-8");

            Transport.send(messageToRestaurant);

            System.out.println("Emails sent successfully!");

            // Εμφάνιση μηνύματος επιτυχίας
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email Sent");
            alert.setHeaderText(null);
            alert.setContentText("Το μήνυμα σας στάλθηκε επιτυχώς!");
            alert.showAndWait();

        } catch (MessagingException | UnsupportedEncodingException mex) {
            mex.printStackTrace();
            // Εμφάνιση μηνύματος αποτυχίας
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Email Error");
            alert.setHeaderText("Failed to send email");
            alert.setContentText("Παρακαλώ ελέγξτε τη σύνδεση στο internet και τις ρυθμίσεις SMTP.");
            alert.showAndWait();
        }
    }

    // Εναλλαγή σελίδων
    public void breakfast_menu1(ActionEvent event) throws IOException { loadScene("breakfast_menu1.fxml", event); }
    public void HomePage(ActionEvent event) throws IOException { loadScene("HomePage.fxml", event); }
    public void OurStory(ActionEvent event) throws IOException { loadScene("OurStory.fxml", event); }
    public void Contact(ActionEvent event) throws IOException { loadScene("Contact.fxml", event); }

    // Γενική μέθοδος φόρτωσης FXML σε νέο Scene
    private void loadScene(String fxmlFile, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
