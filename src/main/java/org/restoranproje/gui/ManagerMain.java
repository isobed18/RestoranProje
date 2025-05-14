package org.restoranproje.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.restoranproje.model.Manager;
import org.restoranproje.service.OrderManager;

public class ManagerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Gerekli servisleri oluştur
        OrderManager orderManager = new OrderManager();
        Manager admin = new Manager("Yusuf", "1234");

        // Controller'a bu servisleri iletmek için FXMLLoader kullan
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/managergui.fxml"));
        Parent root = loader.load();

        // Controller nesnesini al ve servisleri set et
        MainController controller = loader.getController();
        MainController.setServices(orderManager, admin);

        primaryStage.setTitle("Yönetici Paneli");
        primaryStage.setScene(new Scene(root, 800, 600)); // Daha büyük pencere önerilir
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
