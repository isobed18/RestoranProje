package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.beans.property.SimpleStringProperty;

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

    // Initialize method to bind data and load
    @FXML
    public void initialize() {
        // Artık SimpleStringProperty döndüren metodları kullanıyoruz
        colID.setCellValueFactory(data -> data.getValue().orderIdProperty());
        colDetail.setCellValueFactory(data -> data.getValue().detailsProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colTime.setCellValueFactory(data -> data.getValue().timestampProperty());

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
        // Implement your logic here
    }
}
