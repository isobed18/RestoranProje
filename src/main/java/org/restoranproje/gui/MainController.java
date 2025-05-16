package org.restoranproje.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.Pane;
import org.restoranproje.db.OrderDAO;
import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;

import java.io.IOException;

public class MainController {

    @FXML private TableView<Order> order_history;
    @FXML private TableColumn<Order, Integer> colID;
    @FXML private TableColumn<Order, String> colDetail;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TextField status_textbox;
    @FXML private Button cancel_button, complete_button, deliver_button;
    @FXML
    private Button stock_button;
    @FXML
    private Pane view_pane;

    @FXML
    public void initialize() {
        colID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colDetail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDetails()));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        loadOrders();

        order_history.setOnMouseClicked(event -> {
            Order selected = order_history.getSelectionModel().getSelectedItem();
            if (selected != null) {
                status_textbox.setText(selected.getStatus().toString());
            }
        });
    }

    private void loadOrders() {
        ObservableList<Order> allOrders = FXCollections.observableArrayList(OrderDAO.getFullOrderHistory());
        order_history.setItems(null);
        order_history.setItems(allOrders);
    }

    private void updateStatusFromTextField() {
        Order selected = order_history.getSelectionModel().getSelectedItem();
        if (selected == null) {
            status_textbox.setText("Lütfen bir sipariş seçin.");
            return;
        }

        try {
            String statusText = status_textbox.getText().trim().toUpperCase();
            OrderStatus newStatus = OrderStatus.valueOf(statusText);

            selected.setStatus(newStatus);
            OrderDAO.logOrderHistory(selected);

            if (newStatus == OrderStatus.COMPLETED) {
                OrderDAO.saveCompletedOrder(selected);
            }

            loadOrders(); // tabloyu yenile
            order_history.getSelectionModel().clearSelection();
            status_textbox.setText("Durum güncellendi.");

        } catch (IllegalArgumentException e) {
            status_textbox.setText("Geçersiz durum!");
        }
    }

    @FXML void cancel_click(MouseEvent event) {
        status_textbox.setText("CANCELLED");
        updateStatusFromTextField();
    }

    @FXML void complete_click(MouseEvent event) {
        status_textbox.setText("COMPLETED");
        updateStatusFromTextField();
    }

    @FXML void deliver_click(MouseEvent event) {
        status_textbox.setText("DELIVERED");
        updateStatusFromTextField();
    }

    @FXML
    void handleStockButtonClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/stockedit.fxml"));
            Pane stockPane = loader.load();

            view_pane.getChildren().clear();             // önceki içeriği sil
            view_pane.getChildren().add(stockPane);      // stockedit.fxml içeriğini yükle

            // Pane'yi yerleştir: tam kaplasın
            stockPane.setLayoutX(0);
            stockPane.setLayoutY(0);
            stockPane.prefWidthProperty().bind(view_pane.widthProperty());
            stockPane.prefHeightProperty().bind(view_pane.heightProperty());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
