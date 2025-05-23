package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.StockDAO;
import org.restoranproje.model.StockItem;

public class StockEditController {

    @FXML private TableView<StockItem> stock_table;
    @FXML private TableColumn<StockItem, String> colName;
    @FXML private TableColumn<StockItem, Double> colAmount;
    @FXML private TableColumn<StockItem, String> colUnit;
    @FXML private TableColumn<StockItem, Double> colUnitCost;
    @FXML private TableColumn<StockItem, Integer> colId;

    @FXML private TextField add_name, add_amount, add_unit, add_cost;
    @FXML private TextField delete_name;
    @FXML private TextField update_name, update_cost;
    @FXML private TextField edit_name, edit_amount;
    @FXML private Label edit_unit_label;

    private StockItem selectedItem;
    private final StockDAO stockDAO = new StockDAO();
    private final ObservableList<StockItem> stockItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {//sutunlari ayarlama
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colAmount.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getAmount()));
        colUnit.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUnit()));
        colUnitCost.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getUnitCost()));

        loadStockItems();

        stock_table.setOnMouseClicked(event -> {//tiklanan urun yazdirilir textfieldlara
            StockItem selected = stock_table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedItem = selected;
                delete_name.setText(selected.getName());
                update_name.setText(selected.getName());
                update_cost.setText(String.valueOf(selected.getUnitCost()));
                
                // edit icin de yazdirilir
                edit_name.setText(selected.getName());
                edit_amount.setText(String.format("%.2f", selected.getAmount()));
                edit_unit_label.setText(selected.getUnit());
            }
        });
    }

    @FXML
    void handleAmountUpdateClick(MouseEvent event) {
        if (selectedItem == null) {
            showAlert("Lütfen miktarı güncellenecek ürünü tablodan seçin.");
            return;
        }

        try {
            String amountStr = edit_amount.getText().trim();
            if (amountStr.isEmpty()) {
                showAlert("Lütfen yeni miktar girin.");
                return;
            }

            double newAmount = Double.parseDouble(amountStr);
            if (newAmount < 0) {
                showAlert("Miktar negatif olamaz.");
                return;
            }

            // db den miktar guncelleme
            stockDAO.updateStockAmount(selectedItem.getName(), newAmount);
            
            // tabloyu yenile
            loadStockItems();
            

            showSuccess("Miktar başarıyla güncellendi!");
            
            // islem tamamlaninca alanlari temizle
            selectedItem = null;
            edit_name.clear();
            edit_amount.clear();
            edit_unit_label.setText("");

        } catch (NumberFormatException e) {
            showAlert("Geçerli bir miktar girin. Nokta kullanın, virgül değil.");
        } catch (Exception e) {
            showAlert("Hata: " + e.getMessage());
        }
    }

    private void loadStockItems() {//stock itemleri getirirz
        stockItems.setAll(stockDAO.getAllStockItems());
        stock_table.setItems(stockItems);
    }

    @FXML
    void handleAddClick(MouseEvent event) {
        try {
            String name = add_name.getText().trim();
            String unit = add_unit.getText().trim();

            if (name.isEmpty() || unit.isEmpty() ||
                    add_amount.getText().trim().isEmpty() ||
                    add_cost.getText().trim().isEmpty()) {
                showAlert("Tüm alanları doldurun.");
                return;
            }

            double amount = Double.parseDouble(add_amount.getText().trim());
            double cost = Double.parseDouble(add_cost.getText().trim());

            if (amount < 0 || cost < 0) {
                showAlert("Negatif değer girilemez.");
                return;
            }

            StockItem item = new StockItem(name, amount, unit, cost);
            stockDAO.insertStockItem(item);
            loadStockItems();

            add_name.clear();
            add_amount.clear();
            add_unit.clear();
            add_cost.clear();

        } catch (NumberFormatException e) {
            showAlert("Miktar ve fiyat sadece sayı olmalı. Virgül değil, nokta kullan.");
        } catch (Exception e) {
            showAlert("Bir hata oluştu: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteClick(MouseEvent event) {
        String name = delete_name.getText().trim();
        if (!name.isEmpty()) {
            stockDAO.removeStockItem(name);
            loadStockItems();
            delete_name.clear();
            
            // Silinen öğe seçildiyse miktar düzenleme alanlarını temizle
            if (selectedItem != null && selectedItem.getName().equals(name)) {
                selectedItem = null;
                edit_name.clear();
                edit_amount.clear();
                edit_unit_label.setText("");
            }
        } else {
            showAlert("Silmek için ürün adı girin.");
        }
    }

    @FXML
    void handleUpdateClick(MouseEvent event) {
        String name = update_name.getText().trim();
        try {
            double newCost = Double.parseDouble(update_cost.getText().trim());
            if (!name.isEmpty()) {//guncelle ve alanlari temizle
                stockDAO.changeUnitCost(name, newCost);
                loadStockItems();
                update_name.clear();
                update_cost.clear();
            } else {
                showAlert("Ürün adı boş olamaz.");
            }
        } catch (NumberFormatException e) {
            showAlert("Geçersiz fiyat değeri.");
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
