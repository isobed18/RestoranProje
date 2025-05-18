package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.OrderDAO;
import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import org.restoranproje.service.OrderManager;

public class OrderEditController {

    @FXML private TableView<Order> order_history;
    @FXML private TableColumn<Order, Integer> colID;
    @FXML private TableColumn<Order, String> colDetail;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TextArea detail_textarea;
    @FXML private Button cancel_button, complete_button, deliver_button;

    private final OrderManager orderManager = new OrderManager();
    private ObservableList<Order> orders;

    @FXML
    public void initialize() {
        colID.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        colDetail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDetails()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString()));

        loadOrders();

        order_history.setOnMouseClicked(event -> {
            Order selected = order_history.getSelectionModel().getSelectedItem();
            if (selected != null) {
                detail_textarea.setText(selected.getDetails());
                updateButtonStates(selected);
            }
        });
    }

    private void loadOrders() {
        if (orders == null) {
            orders = FXCollections.observableArrayList(OrderDAO.getFullOrderHistory());
            order_history.setItems(orders);
        } else {
            orders.setAll(OrderDAO.getFullOrderHistory());
        }
    }

    private void updateButtonStates(Order order) {
        if (order == null) {
            deliver_button.setDisable(true);
            complete_button.setDisable(true);
            cancel_button.setDisable(true);
            return;
        }

        // Enable/disable buttons based on current order status
        switch (order.getStatus()) {
            case NEW:
                deliver_button.setDisable(true);
                complete_button.setDisable(false);
                cancel_button.setDisable(false);
                break;
            case COMPLETED:
                deliver_button.setDisable(false);
                complete_button.setDisable(true);
                cancel_button.setDisable(false);
                break;
            case DELIVERED:
            case CANCELLED:
                deliver_button.setDisable(true);
                complete_button.setDisable(true);
                cancel_button.setDisable(true);
                break;
            default:
                deliver_button.setDisable(true);
                complete_button.setDisable(false);
                cancel_button.setDisable(false);
        }
    }

    @FXML 
    void cancel_click(MouseEvent event) {
        Order selected = order_history.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("İptal etmek için sipariş seçin.");
            return;
        }

        // Check if order is already cancelled
        if (selected.getStatus() == OrderStatus.CANCELLED) {
            showWarning("Bu sipariş zaten iptal edilmiş.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        // Check if order is already delivered
        if (selected.getStatus() == OrderStatus.DELIVERED) {
            showWarning("Teslim edilmiş sipariş iptal edilemez.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        selected.setStatus(OrderStatus.CANCELLED);
        OrderDAO.logOrderHistory(selected);
        
        // Update the order in the table
        int index = orders.indexOf(selected);
        if (index >= 0) {
            orders.set(index, selected);
        }
        
        detail_textarea.clear();
        updateButtonStates(null);
        order_history.getSelectionModel().clearSelection();
    }

    @FXML 
    void complete_click(MouseEvent event) {
        Order selected = order_history.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Tamamlamak için sipariş seçin.");
            return;
        }

        // Check if order is already completed or delivered
        if (selected.getStatus() == OrderStatus.COMPLETED || selected.getStatus() == OrderStatus.DELIVERED) {
            showWarning("Bu sipariş zaten hazırlanmış.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        // Check if order is cancelled
        if (selected.getStatus() == OrderStatus.CANCELLED) {
            showWarning("İptal edilmiş sipariş hazırlanamaz.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        selected.setStatus(OrderStatus.COMPLETED);
        OrderDAO.logOrderHistory(selected);
        OrderDAO.saveCompletedOrder(selected);
        
        // Update the order in the table
        int index = orders.indexOf(selected);
        if (index >= 0) {
            orders.set(index, selected);
        }
        
        detail_textarea.clear();
        updateButtonStates(null);
        order_history.getSelectionModel().clearSelection();
    }

    @FXML 
    void deliver_click(MouseEvent event) {
        Order selected = order_history.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Teslim etmek için sipariş seçin.");
            return;
        }

        // Check if order is already delivered
        if (selected.getStatus() == OrderStatus.DELIVERED) {
            showWarning("Bu sipariş zaten teslim edilmiş.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        // Check if order is not completed yet
        if (selected.getStatus() != OrderStatus.COMPLETED) {
            showWarning("Hazırlanmamış sipariş teslim edilemez.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        // Check if order is cancelled
        if (selected.getStatus() == OrderStatus.CANCELLED) {
            showWarning("İptal edilmiş sipariş teslim edilemez.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
            return;
        }

        selected.setStatus(OrderStatus.DELIVERED);
        OrderDAO.logOrderHistory(selected);
        
        // Update the order in the table
        int index = orders.indexOf(selected);
        if (index >= 0) {
            orders.set(index, selected);
        }
        
        detail_textarea.clear();
        updateButtonStates(null);
        order_history.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("İşlem Yapılamaz");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
