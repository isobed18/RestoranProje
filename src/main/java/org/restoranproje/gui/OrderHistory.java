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
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
public class OrderHistory {
    private final SimpleStringProperty orderId;
    private final SimpleStringProperty details;
    private final SimpleStringProperty status;
    private final SimpleStringProperty timestamp;

    public OrderHistory(String orderId, String details, String status, String timestamp) {
        this.orderId = new SimpleStringProperty(orderId);
        this.details = new SimpleStringProperty(details);
        this.status = new SimpleStringProperty(status);
        this.timestamp = new SimpleStringProperty(timestamp);
    }

    // Bu metodlar artık SimpleStringProperty döndürüyor
    public SimpleStringProperty orderIdProperty() {
        return orderId;
    }

    public SimpleStringProperty detailsProperty() {
        return details;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public SimpleStringProperty timestampProperty() {
        return timestamp;
    }
}

