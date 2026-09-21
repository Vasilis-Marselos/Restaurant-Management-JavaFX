package com.example.RestaurantManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("HomePage.fxml")));
            Scene scene = new Scene(root);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/RestaurantManagement/image/logo1.png"))));
            stage.setTitle("Maison de Goût Restaurant");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
