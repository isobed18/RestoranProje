package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.restoranproje.model.User;
import org.restoranproje.model.UserType;
import org.restoranproje.db.UserDAO;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<UserType> userTypeComboBox;

    @FXML
    public void initialize() {
        userTypeComboBox.getItems().addAll(UserType.values());
        userTypeComboBox.setValue(UserType.WAITER); // default type waiter
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();//textfieldlardan bilgiler alinir
        String password = passwordField.getText().trim();
        UserType selectedType = userTypeComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {//bos kalamaz
            showError("Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        // db den kotnrol edilir kullanici
        if (UserDAO.validateUser(username, password, selectedType)) {
            try {
                openAppropriateInterface(selectedType, username, password);
            } catch (IOException e) {
                showError("Hata", "Arayüz yüklenirken hata oluştu: " + e.getMessage());
            }
        } else {
            showError("Giriş Başarısız", "Kullanıcı adı, şifre veya kullanıcı tipi hatalı!");
        }
    }

    private void openAppropriateInterface(UserType userType, String username, String password) throws IOException {
        String fxmlPath;
        String title;
        FXMLLoader loader = new FXMLLoader();

        switch (userType) {//kullanici tipine gore acar
            case MANAGER:
                fxmlPath = "/org/restoranproje/gui/managergui.fxml";
                title = "Yönetici Arayüzü";
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                break;
            case WAITER:
                fxmlPath = "/org/restoranproje/gui/waitergui.fxml";
                title = "Garson Arayüzü";
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                WaiterGuiController controller = loader.getController();
                controller.setWaiter(username, password);
                loadInterface(title, root);
                return;
            case CHEF:
                fxmlPath = "/org/restoranproje/gui/chefgui.fxml";
                title = "Şef Arayüzü";
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                break;
            default:
                showError("Hata", "Geçersiz kullanıcı tipi!");
                return;
        }

        Parent root = loader.load();
        loadInterface(title, root);
    }

    private void loadInterface(String title, Parent root) {
        Stage currentStage = (Stage) usernameField.getScene().getWindow();


        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setTitle(title);
        currentStage.setMaximized(true);
        currentStage.show();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}