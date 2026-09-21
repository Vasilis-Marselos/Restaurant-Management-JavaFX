module com.example.RestaurantManagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.mail;
    requires com.github.librepdf.openpdf;

    opens com.example.RestaurantManagement to javafx.fxml;
    exports com.example.RestaurantManagement;
}