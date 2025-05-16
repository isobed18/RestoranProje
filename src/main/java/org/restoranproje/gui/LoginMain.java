package org.restoranproje.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.restoranproje.model.User;


public class LoginMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/login_gui.fxml"));
        Parent root = loader.load();

        stage.setTitle("Login");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
