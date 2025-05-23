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
        // order
        orderService = OrderService.getInstance();

        // tablonu  sütun isimlerini ayarlama
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("order")); //
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // siparişleri tabloya yükle
        loadOrders();

        // sipariş secili değilken tamamla butonu gizlenir
        completeButton.disableProperty().bind(ordersTable.getSelectionModel().selectedItemProperty().isNull());

        // sipariş filtreleme aşçı sadece ona gelen spiarşleri görür
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
            // status Completed
            selectedOrder.setStatus(OrderStatus.COMPLETED);
            
            // order update
            orderService.updateOrder(selectedOrder);
            
            // tabloyu yenileriz
            loadOrders();
        }
    }
    //siparişleri tabloya getirmek için
    private void loadOrders() {
        List<Order> newOrders = orderService.getAllOrders().stream()
            .filter(order -> order.getStatus() == OrderStatus.NEW)
            .collect(Collectors.toList());
        ordersTable.setItems(FXCollections.observableArrayList(newOrders));
    }
} 