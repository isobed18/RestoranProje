package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import org.restoranproje.service.OrderService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChefController implements Initializable {

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, Integer> idColumn;

    @FXML
    private TableColumn<Order, String> orderIdColumn;

    @FXML
    private TableColumn<Order, String> detailsColumn;

    @FXML
    private TableColumn<Order, OrderStatus> statusColumn;

    @FXML
    private Button completeButton;

    private OrderService orderService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the order service
        orderService = OrderService.getInstance();

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Using same ID since there's only one in the model
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load orders from database or service
        loadOrders();

        // Disable complete button when no row is selected
        completeButton.disableProperty().bind(ordersTable.getSelectionModel().selectedItemProperty().isNull());

        // Add listener to filter out completed and delivered orders
        ordersTable.setItems(FXCollections.observableArrayList(
            orderService.getAllOrders().stream()
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleCompleteOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Update order status to completed
            selectedOrder.setStatus(OrderStatus.COMPLETED);
            
            // Update in database or service
            orderService.updateOrder(selectedOrder);
            
            // Refresh table
            loadOrders();
        }
    }

    private void loadOrders() {
        List<Order> newOrders = orderService.getAllOrders().stream()
            .filter(order -> order.getStatus() == OrderStatus.NEW)
            .collect(Collectors.toList());
        ordersTable.setItems(FXCollections.observableArrayList(newOrders));
    }
} 