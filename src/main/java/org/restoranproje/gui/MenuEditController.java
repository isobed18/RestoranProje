package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.MenuDAO;
import org.restoranproje.model.MenuItem;
import org.restoranproje.model.MenuItemType;
import org.restoranproje.model.StockItem;

import java.util.ArrayList;
import java.util.List;

public class MenuEditController {

    @FXML private TableView<MenuItem> menu_table;
    @FXML private TableColumn<MenuItem, String> colName;
    @FXML private TableColumn<MenuItem, String> colDescription;
    @FXML private TableColumn<MenuItem, String> colType;
    @FXML private TableColumn<MenuItem, Double> colPrice;

    @FXML private TextField add_name, add_description, add_price;
    @FXML private ComboBox<MenuItemType> add_type;
    @FXML private TextField delete_name;
    @FXML private TextField edit_name;
    @FXML private TextField edit_price;

    private MenuItem selectedItem;
    private final MenuDAO menuDAO = new MenuDAO();
    private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // ComboBox türlerini doldur
        add_type.setItems(FXCollections.observableArrayList(MenuItemType.values()));

        // Tablo kolonlarını bağla
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colDescription.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescription()));
        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType().toString()));
        colPrice.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrice()));

        // Tabloya veri yükle
        loadMenuItems();

        // Satıra tıklanınca delete_name e ve edit_name e yaz
        menu_table.setOnMouseClicked(event -> {
            MenuItem selected = menu_table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedItem = selected;
                delete_name.setText(selected.getName());
                edit_name.setText(selected.getName());
                edit_price.setText(String.format("%.2f", selected.getPrice()));
            }
        });
    }

    private void loadMenuItems() {
        menuItems.setAll(menuDAO.getAllMenuItems());
        menu_table.setItems(menuItems);
    }

    @FXML
    void handleUpdatePriceClick(MouseEvent event) {
        if (selectedItem == null) {
            showAlert("Lütfen fiyatı güncellenecek ürünü tablodan seçin.");
            return;
        }

        try {
            String priceStr = edit_price.getText().trim();
            if (priceStr.isEmpty()) {
                showAlert("Lütfen yeni fiyat girin.");
                return;
            }

            double newPrice = Double.parseDouble(priceStr);
            if (newPrice < 0) {
                showAlert("Fiyat negatif olamaz.");
                return;
            }

            // db den fiyati guncelle
            menuDAO.updateMenuItemPrice(selectedItem.getName(), newPrice);
            
            // tabloyu yenile
            loadMenuItems();
            
            // guncellenince
            showSuccess("Fiyat başarıyla güncellendi!");

            selectedItem = null;
            edit_name.clear();
            edit_price.clear();

        } catch (NumberFormatException e) {
            showAlert("Geçerli bir fiyat girin. Nokta kullanın, virgül değil.");
        } catch (Exception e) {
            showAlert("Hata: " + e.getMessage());
        }
    }

    @FXML
    void handleAddClick(MouseEvent event) {
        try {
            String name = add_name.getText().trim();
            String description = add_description.getText().trim();
            MenuItemType type = add_type.getValue();
            double price = Double.parseDouble(add_price.getText().trim());

            if (name.isEmpty() || description.isEmpty() || type == null) {
                showAlert("Lütfen tüm alanları doldurun.");
                return;
            }

            // Şu anda boş bir malzeme listesi ile ekliyoruz
            ArrayList<StockItem> emptyIngredients = new ArrayList<>();
            MenuItem item = new MenuItem(name, description, type, price, emptyIngredients);

            // Boş bir stockItemId listesi gönderiyoruz
            menuDAO.insertMenuItem(item, new ArrayList<>());

            loadMenuItems();

            add_name.clear();
            add_description.clear();
            add_price.clear();
            add_type.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            showAlert("Fiyat sayı olmalı. Nokta kullanın, virgül değil.");
        } catch (Exception e) {
            showAlert("Hata: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteClick(MouseEvent event) {
        String name = delete_name.getText().trim();
        if (!name.isEmpty()) {
            menuDAO.removeMenuItemByName(name);
            loadMenuItems();
            delete_name.clear();
            // Ayrıca silinen öğe seçildiyse fiyat düzenleme alanlarını da temizle
            if (selectedItem != null && selectedItem.getName().equals(name)) {
                selectedItem = null;
                edit_name.clear();
                edit_price.clear();
            }
        } else {
            showAlert("Lütfen silinecek ürün adını girin.");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Başarılı");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
