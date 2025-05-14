package org.restoranproje.gui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class ManagerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // FXML dosyasını yükle
        Parent root = FXMLLoader.load(getClass().getResource("/org/restoranproje/gui/managergui.fxml"));

        primaryStage.setTitle("JavaFX Arayüz Örneği");
        primaryStage.setScene(new Scene(root, 400, 300)); // Genişlik ve yükseklik
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // JavaFX uygulamasını başlat
    }
}
