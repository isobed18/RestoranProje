package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.DatabaseManager;
import org.restoranproje.model.Chef;
import org.restoranproje.model.User;

import java.sql.*;

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
    public void initialize() {//sef tablosu sutunlari ayarlanir
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colPassword.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassword()));
        colRole.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUserType().toString()));

        loadChefs();//tabloyu guncelle

        chef_table.setOnMouseClicked(event -> {//tablodan sectigimiz sef silinecek olup textfielda yazilir
            User selected = chef_table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                delete_name.setText(selected.getName());
            }
        });
    }

    private void loadChefs() {
        chefList.clear();//listeyi temizle

        String query = "SELECT name, password, role FROM users WHERE role = 'CHEF'";//userdan gerekli veriler
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                // Veritabanından gelen kullanıcıyı tekrar kaydetme!
                Chef chef = new Chef(name, password, false); //  Kaydetmeden oluşturulur saveToDB
                chefList.add(chef);
            }

            chef_table.setItems(chefList);

        } catch (SQLException e) {
            showAlert("Veritabanından şefler alınamadı: " + e.getMessage());
        }
    }


    @FXML
    void handleAddClick(MouseEvent event) {//name ve password textfielddan alinir
        String name = add_name.getText().trim();
        String password = add_password.getText().trim();

        if (name.isEmpty() || password.isEmpty()) {//textfield bos olamaz
            showAlert("Ad ve şifre boş olamaz.");
            return;
        }

        // Veritabanına kaydeden constructor. Constructor Overloading
        Chef newChef = new Chef(name, password);

        loadChefs(); //Tüm veritabanını okuyup tek seferlik yazıyor

        // textfieldlari temizle
        add_name.clear();
        add_password.clear();
    }

    @FXML
    void handleDeleteClick(MouseEvent event) {
        String name = delete_name.getText().trim();//text fielddan bilgi alinir

        if (name.isEmpty()) {
            showAlert("Silmek için bir ad girin.");
            return;
        }

        String query = "DELETE FROM users WHERE name = ? AND role = 'CHEF'";//db den silinir

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            int affectedRows = pstmt.executeUpdate();//silinen satir sayisini dondurur

            if (affectedRows > 0) {
                showAlert("Şef başarıyla silindi.");
            } else {
                showAlert("Şef bulunamadı. Belki isim yanlış girildi?");
            }

            loadChefs();           // tabloyu güncelle
            delete_name.clear();   // alanı temizle

        } catch (SQLException e) {
            showAlert("Şef silinirken hata: " + e.getMessage());
        }
    }




    private void showAlert(String msg) {//gerekli uyarilar icin yardimci metot
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
