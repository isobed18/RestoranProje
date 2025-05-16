package org.restoranproje.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import org.restoranproje.model.Chef;
import org.restoranproje.model.Order;

public class ChefguiMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/chefgui.fxml"));
        Parent root = loader.load();

        stage.setTitle("Chef Page");
        stage.setScene(new Scene(root, 844, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
