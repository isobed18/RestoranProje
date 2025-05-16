package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import org.restoranproje.model.User;
import org.restoranproje.model.UserType;
import org.restoranproje.db.UserDAO;

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
        String username = usernameField.getText();
        String password = passwordField.getText();
        UserType selectedType = userTypeComboBox.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        // Validate against database
        if (UserDAO.validateUser(username, password, selectedType)) {
            showAlert("Success", "Login successful!");
            // TODO: Open the appropriate page based on user type
        } else {
            showAlert("Error", "Invalid username, password, or user type!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}