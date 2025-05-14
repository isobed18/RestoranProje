package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

import org.restoranproje.model.*;
import org.restoranproje.service.OrderManager;

import java.sql.*;

public class MainController {

    @FXML
    private Button chef_button;

    @FXML
    private TableColumn<OrderHistory, String> colDetail;

    @FXML
    private TableColumn<OrderHistory, String> colID;

    @FXML
    private TableColumn<OrderHistory, String> colStatus;

    @FXML
    private TableColumn<OrderHistory, String> colTime;

    @FXML
    private Pane inner_pane;

    @FXML
    private Button menu_button;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private Button order_button;

    @FXML
    private TableView<OrderHistory> order_history;

    @FXML
    private Button order_history_button;

    @FXML
    private TextField order_history_textbox;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private Button stock_button;

    @FXML
    private Button waiter_button;

    // Sipariş yönetimi ve kullanıcı
    private static OrderManager orderManager;
    private static Manager manager;

    // Main'den çağrılması için static setter
    public static void setServices(OrderManager om, Manager m) {
        orderManager = om;
        manager = m;
    }

    // Seçili Order ID
    private int selectedOrderId = -1;

    @FXML
    public void initialize() {
        colID.setCellValueFactory(data -> data.getValue().orderIdProperty());
        colDetail.setCellValueFactory(data -> data.getValue().detailsProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colTime.setCellValueFactory(data -> data.getValue().timestampProperty());

        // Tablo tıklanınca TextField'e status yaz
        order_history.setOnMouseClicked((MouseEvent event) -> {
            OrderHistory selected = order_history.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedOrderId = Integer.parseInt(selected.orderIdProperty().getValue());
                order_history_textbox.setText(selected.statusProperty().get());
            }
        });

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        ObservableList<OrderHistory> data = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:./src/main/resources/database/restoran.db";
        String sql = "SELECT order_id, details, status, timestamp FROM order_history";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrderHistory order = new OrderHistory(
                        rs.getString("order_id"),
                        rs.getString("details"),
                        rs.getString("status"),
                        rs.getString("timestamp")
                );
                data.add(order);
            }

            order_history.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void save_change_order(MouseEvent event) {
        if (selectedOrderId == -1) {
            System.out.println("Lütfen bir sipariş seçin.");
            return;
        }

        String newStatusStr = order_history_textbox.getText().toUpperCase().trim();
        try {
            OrderStatus newStatus = OrderStatus.valueOf(newStatusStr);
            manager.changeOrderStatus(orderManager, selectedOrderId, newStatus);
            System.out.println("Durum güncellendi.");
            loadDataFromDatabase(); // Tabloyu yenile
        } catch (IllegalArgumentException e) {
            System.out.println("Geçersiz durum girdiniz.");
        }
    }
}
