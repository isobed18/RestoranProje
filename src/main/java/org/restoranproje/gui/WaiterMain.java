package org.restoranproje.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.restoranproje.model.Waiter;
import org.restoranproje.model.Order;
import org.restoranproje.model.MenuItem;


public class WaiterMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/waitergui.fxml"));
        Parent root = loader.load();

        stage.setTitle("Waiter Page");
        stage.setScene(new Scene(root, 910, 587));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
