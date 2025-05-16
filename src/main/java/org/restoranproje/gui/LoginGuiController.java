package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.restoranproje.db.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginGuiController {

    @FXML
    private CheckBox chef_cb;

    @FXML
    private Button login_button;

    @FXML
    private CheckBox manager_cb;

    @FXML
    private TextField password_tb;

    @FXML
    private TextField username_tb;

    @FXML
    private CheckBox waiter_cb;

    @FXML
    void login_system(MouseEvent event) {
        String username = username_tb.getText();
        String password = password_tb.getText();

        String selectedRole = null;
        if (manager_cb.isSelected()) selectedRole = "Manager";
        else if (waiter_cb.isSelected()) selectedRole = "Waiter";
        else if (chef_cb.isSelected()) selectedRole = "Chef";

        if (selectedRole == null) {
            showAlert("Lütfen bir rol seçiniz.");
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM users WHERE name = ? AND password = ? AND type = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, selectedRole);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                openRoleScreen(selectedRole);
            } else {
                showAlert("Hatalı kullanıcı adı, şifre veya rol.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Veritabanı hatası: " + e.getMessage());
        }
    }

    private void openRoleScreen(String role) {
        try {
            String fxmlFile = switch (role) {
                case "Manager" -> "/org/restoranproje/gui/managergui.fxml";
                case "Waiter" -> "/org/restoranproje/gui/waitergui.fxml";
                case "Chef" -> "/org/restoranproje/gui/chefgui.fxml";
                default -> throw new IllegalStateException("Unexpected value: " + role);
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) login_button.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Sayfa yüklenemedi: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Giriş Hatası");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
