package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.restoranproje.db.MenuDAO;
import org.restoranproje.model.MenuItem;
import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import org.restoranproje.model.Waiter;
import org.restoranproje.service.OrderManager;

public class WaiterGuiController {

    @FXML private TableView<MenuItem> menu_table;
    @FXML private TableColumn<MenuItem, String> colMenuName;
    @FXML private TableColumn<MenuItem, String> colMenuDescription;
    @FXML private TableColumn<MenuItem, String> colMenuType;
    @FXML private TableColumn<MenuItem, Double> colMenuPrice;

    @FXML private TableView<Order> order_table;
    @FXML private TableColumn<Order, Integer> colOrderID;
    @FXML private TableColumn<Order, String> colOrderDetails;
    @FXML private TableColumn<Order, String> colOrderStatus;

    @FXML private TextArea detail_textarea;
    @FXML private Button add_button;
    @FXML private Button deliver_button;
    @FXML private Button cancel_button;

    private ObservableList<MenuItem> menuItems;
    private ObservableList<Order> orders;

    private final OrderManager orderManager = new OrderManager();
    private final Waiter waiter = new Waiter("Zeynep", "1234"); // or login ekranından gelsin

    @FXML
    public void initialize() {
        orderManager.addObserver(waiter);

        // Menu Table
        colMenuName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        colMenuDescription.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescription()));
        colMenuType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType().toString()));
        colMenuPrice.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrice()));

        menuItems = FXCollections.observableArrayList(new MenuDAO().getAllMenuItems());
        menu_table.setItems(menuItems);

        // Order Table
        colOrderID.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));
        colOrderDetails.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDetails()));
        colOrderStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatus().toString()));

        orders = FXCollections.observableArrayList(orderManager.getAllOrders());
        order_table.setItems(orders);

        order_table.setOnMouseClicked(e -> {
            Order selected = order_table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                detail_textarea.setText(selected.getDetails());
            }
        });
    }

    @FXML
    void handleAddClick(MouseEvent event) {
        MenuItem selected = menu_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Lütfen menüden bir ürün seçin.");
            return;
        }

        int newId = orderManager.getAllOrders().size() + 1;
        Order newOrder = new Order(newId, selected.getName(), new java.util.ArrayList<>());
        waiter.takeOrder(orderManager, newOrder);
        refreshOrders();
    }

    @FXML
    void handleDeliverClick(MouseEvent event) {
        Order selected = order_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Teslim etmek için sipariş seçin.");
            return;
        }
        waiter.deliverOrder(orderManager, selected.getId());
        refreshOrders();
    }

    @FXML
    void handleCancelClick(MouseEvent event) {
        Order selected = order_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("İptal etmek için sipariş seçin.");
            return;
        }
        waiter.cancelOrder(orderManager, selected.getId());
        refreshOrders();
    }

    private void refreshOrders() {
        orders.setAll(orderManager.getAllOrders());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}