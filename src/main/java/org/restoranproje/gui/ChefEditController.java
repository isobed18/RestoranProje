package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.UserDAO;
import org.restoranproje.model.Chef;
import org.restoranproje.model.User;
import org.restoranproje.model.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChefEditController {

    @FXML private TableView<User> chef_table;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colPassword;
    @FXML private TableColumn<User, String> colRole;

    @FXML private TextField add_name;
    @FXML private TextField add_password;
    @FXML private TextField delete_name;

    private final ObservableList<User> chefList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colPassword.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassword()));
        colRole.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUserType().toString()));

        loadChefs();

        chef_table.setOnMouseClicked(event -> {
            User selected = chef_table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                delete_name.setText(selected.getName());
            }
        });
    }

    private void loadChefs() {
        chefList.clear();

        String query = "SELECT name, password, role FROM users WHERE role = 'CHEF'";
        try (Connection conn = org.restoranproje.db.DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                User chef = new Chef(name, password); // Chef sınıfı User'dan türediği için çalışır
                chefList.add(chef);
            }

            chef_table.setItems(chefList);

        } catch (SQLException e) {
            showAlert("Veritabanından şefler alınamadı: " + e.getMessage());
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

        Chef newChef = new Chef(name, password);
        UserDAO.saveUser(newChef);

        loadChefs(); // tabloyu yenile
        add_name.clear();
        add_password.clear();
    }

    @FXML
    void handleDeleteClick(MouseEvent event) {
        String name = delete_name.getText().trim();

        if (name.isEmpty()) {
            showAlert("Silmek için ad girin.");
            return;
        }

        String query = "DELETE FROM users WHERE name = ? AND role = 'CHEF'";

        try (Connection conn = org.restoranproje.db.DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

            loadChefs();
            delete_name.clear();

        } catch (SQLException e) {
            showAlert("Şef silinirken hata: " + e.getMessage());
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
