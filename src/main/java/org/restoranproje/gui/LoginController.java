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
        userTypeComboBox.setValue(UserType.WAITER); // Default value
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        UserType selectedType = userTypeComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        // Validate against database
        if (UserDAO.validateUser(username, password, selectedType)) {
            try {
                openAppropriateInterface(selectedType);
            } catch (IOException e) {
                showError("Hata", "Arayüz yüklenirken hata oluştu: " + e.getMessage());
            }
        } else {
            showError("Giriş Başarısız", "Kullanıcı adı, şifre veya kullanıcı tipi hatalı!");
        }
    }

    private void openAppropriateInterface(UserType userType) throws IOException {
        String fxmlPath;
        String title;

        switch (userType) {
            case MANAGER:
                fxmlPath = "/org/restoranproje/gui/managergui.fxml";
                title = "Yönetici Arayüzü";
                break;
            case WAITER:
                fxmlPath = "/org/restoranproje/gui/waitergui.fxml";
                title = "Garson Arayüzü";
                break;
            case CHEF:
                fxmlPath = "/org/restoranproje/gui/chefgui.fxml";
                title = "Şef Arayüzü";
                break;
            default:
                showError("Hata", "Geçersiz kullanıcı tipi!");
                return;
        }

        // Load the new interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Get the current stage
        Stage currentStage = (Stage) usernameField.getScene().getWindow();

        // Create and configure new scene
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