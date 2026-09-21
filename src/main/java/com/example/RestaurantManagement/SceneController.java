package com.example.RestaurantManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Κεντρικός controller για την αλλαγή των σκηνών (σελίδων) της εφαρμογής
public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Μέθοδοι για φόρτωση διαφορετικών FXML σελίδων ανά κατηγορία μενού ή σελίδα
    public void breakfast_menu1(ActionEvent event) throws IOException { loadScene("breakfast_menu1.fxml", event); }
    public void breakfast_menu2(ActionEvent event) throws IOException { loadScene("breakfast_menu2.fxml", event); }
    public void maincourse_menu1(ActionEvent event) throws IOException { loadScene("maincourse_menu1.fxml", event); }
    public void maincourse_menu2(ActionEvent event) throws IOException { loadScene("maincourse_menu2.fxml", event); }
    public void appetizers_menu1(ActionEvent event) throws IOException { loadScene("appetizers_menu1.fxml", event); }
    public void appetizers_menu2(ActionEvent event) throws IOException { loadScene("appetizers_menu2.fxml", event); }
    public void beverages_menu1(ActionEvent event) throws IOException { loadScene("beverages_menu1.fxml", event); }
    public void HomePage(ActionEvent event) throws IOException { loadScene("HomePage.fxml", event); }
    public void OurStory(ActionEvent event) throws IOException { loadScene("OurStory.fxml", event); }
    public void Contact(ActionEvent event) throws IOException { loadScene("Contact.fxml", event); }
    public void BreakfastOrder(ActionEvent event) throws IOException { loadScene("Breakfast_Order.fxml", event); }
    public void MainCourseOrder(ActionEvent event) throws IOException { loadScene("MainCourse_Order.fxml", event); }
    public void AppetizersOrder(ActionEvent event) throws IOException { loadScene("Appetizers_Order.fxml", event); }

    private void loadScene(String fxmlFile, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
