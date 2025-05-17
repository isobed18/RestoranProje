package org.restoranproje.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RestaurantApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Restaurant Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Lock size for login screen
        primaryStage.centerOnScreen(); // Center the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}