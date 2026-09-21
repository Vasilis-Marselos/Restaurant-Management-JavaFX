package com.example.RestaurantManagement;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePage {

    @FXML private AnchorPane sliderPane;

    @FXML private ImageView imageView;

    private final List<Image> images = new ArrayList<>();
    private int currentIndex = 0;
    private Timeline timeline;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Καλείται αυτόματα όταν φορτωθεί το FXML
    @FXML
    public void initialize() {
        loadImages();
        startSlider();
    }

    // Φορτώνει τις εικόνες στη λίστα
    private void loadImages() {
        images.add(new Image(getClass().getResourceAsStream("image/main.jpg")));
        images.add(new Image(getClass().getResourceAsStream("image/second.JPG")));
        images.add(new Image(getClass().getResourceAsStream("image/third.JPG")));
        images.add(new Image(getClass().getResourceAsStream("image/extra.JPG")));
    }

    // Εκκινεί το slider που αλλάζει εικόνες κάθε 5 δευτερόλεπτα
    private void startSlider() {
        if (images.isEmpty()) return;

        imageView.setImage(images.get(currentIndex));

        timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            currentIndex = (currentIndex + 1) % images.size();
            imageView.setImage(images.get(currentIndex));
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Μεταβαίνει σε διαφορετικές σελίδες της εφαρμογής
    public void breakfast_menu1(ActionEvent event) throws IOException { loadScene("breakfast_menu1.fxml", event); }
    public void HomePage(ActionEvent event) throws IOException { loadScene("HomePage.fxml", event); }
    public void OurStory(ActionEvent event) throws IOException { loadScene("OurStory.fxml", event); }
    public void Contact(ActionEvent event) throws IOException { loadScene("Contact.fxml", event); }
    public void Reservation(ActionEvent event) throws IOException { loadScene("RestaurantLayout.fxml", event); }

    // Βοηθητική μέθοδος για φόρτωση FXML και αλλαγή σκηνής
    private void loadScene(String fxmlFile, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}