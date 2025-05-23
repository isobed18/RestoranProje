package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.DatabaseManager;
import org.restoranproje.model.User;
import org.restoranproje.model.UserType;
import org.restoranproje.model.Waiter;

import java.sql.*;

public class WaiterEditController {

    @FXML private TableView<User> waiter_table;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colPassword;
    @FXML private TableColumn<User, String> colRole;

    @FXML private TextField add_name;
    @FXML private TextField add_password;
    @FXML private TextField delete_name;

    private final ObservableList<User> waiterList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {//sutunlari ayarla
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colPassword.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassword()));
        colRole.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUserType().toString()));

        loadWaiters();

        waiter_table.setOnMouseClicked(event -> {//tablodan secebilme
            User selected = waiter_table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                delete_name.setText(selected.getName());
            }
        });
    }

    private void loadWaiters() {//sadece waiterlar getirilir
        waiterList.clear();

        String query = "SELECT name, password, role FROM users WHERE role = 'WAITER'";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");

                Waiter waiter = new Waiter(name, password, false); // DB'ye tekrar yazmasın
                waiterList.add(waiter);
            }

            waiter_table.setItems(waiterList);

        } catch (SQLException e) {
            showAlert("Veritabanından garsonlar alınamadı: " + e.getMessage());
        }
    }

    @FXML
    void handleAddClick(MouseEvent event) {
        String name = add_name.getText().trim();
        String password = add_password.getText().trim();

        if (name.isEmpty() || password.isEmpty()) {
            showAlert("Ad ve şifre boş olamaz.");
            return;
        }

        new Waiter(name, password); // DB'ye kaydeden constructor
        loadWaiters();

        add_name.clear();
        add_password.clear();
    }

    @FXML
    void handleDeleteClick(MouseEvent event) {
        String name = delete_name.getText().trim();

        if (name.isEmpty()) {
            showAlert("Silmek için bir ad girin.");
            return;
        }

        String query = "DELETE FROM users WHERE name = ? AND role = 'WAITER'";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                showAlert("Garson silindi.");
            } else {
                showAlert("Garson bulunamadı.");
            }

            loadWaiters();
            delete_name.clear();

        } catch (SQLException e) {
            showAlert("Silme hatası: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
