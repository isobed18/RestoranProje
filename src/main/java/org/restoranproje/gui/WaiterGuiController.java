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
import org.restoranproje.model.StockItem;
import org.restoranproje.service.OrderManager;

import java.util.ArrayList;
import java.util.List;

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
    @FXML private Spinner<Integer> quantity_spinner;

    private ObservableList<MenuItem> menuItems;
    private ObservableList<Order> orders;
    private List<MenuItem> currentOrderItems;

    private final OrderManager orderManager = new OrderManager();
    private final Waiter waiter = new Waiter("Zeynep", "1234"); // or login ekranından gelsin

    @FXML
    public void initialize() {
        orderManager.addObserver(waiter);
        currentOrderItems = new ArrayList<>();

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

        // Quantity Spinner
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        quantity_spinner.setValueFactory(valueFactory);

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

        int quantity = quantity_spinner.getValue();
        for (int i = 0; i < quantity; i++) {
            currentOrderItems.add(selected);
        }

        // Show current order items in detail textarea
        StringBuilder details = new StringBuilder("Mevcut Sipariş:\n");
        for (MenuItem item : currentOrderItems) {
            details.append("- ").append(item.getName()).append(" (₺").append(item.getPrice()).append(")\n");
        }
        detail_textarea.setText(details.toString());
    }

    @FXML
    void handleSubmitOrder(MouseEvent event) {
        if (currentOrderItems.isEmpty()) {
            showAlert("Lütfen siparişe ürün ekleyin.");
            return;
        }

        StringBuilder details = new StringBuilder("Sipariş Detayları:\n");
        double total = 0;
        for (MenuItem item : currentOrderItems) {
            details.append("- ").append(item.getName()).append(" (₺").append(item.getPrice()).append(")\n");
            total += item.getPrice();
        }
        details.append("\nToplam: ₺").append(total);

        Order newOrder = new Order(0, details.toString(), new ArrayList<>(currentOrderItems));
        
        // Check if there's enough stock before submitting
        if (!orderManager.getStockManager().canFulfillOrder(newOrder)) {
            showStockWarning(newOrder);
            return;
        }

        waiter.takeOrder(orderManager, newOrder);
        currentOrderItems.clear();
        detail_textarea.clear();
        refreshOrders();
    }

    @FXML
    void handleDeliverClick(MouseEvent event) {
        Order selected = order_table.getSelectionModel().getSelectedItem();
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

        // Check if order is cancelled
        if (selected.getStatus() == OrderStatus.CANCELLED) {
            showWarning("İptal edilmiş sipariş teslim edilemez.", 
                       "Sipariş ID: " + selected.getId() + "\nMevcut Durum: " + selected.getStatus());
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

    private void showWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("İşlem Yapılamaz");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showStockWarning(Order order) {
        StringBuilder warningMsg = new StringBuilder("Yetersiz stok!\n\nEksik olan malzemeler:\n");
        List<StockItem> allStock = orderManager.getStockManager().getAllStockItems();
        
        // Get the required stock items for the order
        for (MenuItem item : order.getItems()) {
            for (StockItem neededItem : item.getItems()) {
                // Find matching stock item
                StockItem currentStock = null;
                for (StockItem stockItem : allStock) {
                    if (stockItem.getId() == neededItem.getId()) {
                        currentStock = stockItem;
                        break;
                    }
                }

                if (currentStock == null || currentStock.getAmount() < neededItem.getAmount()) {
                    warningMsg.append("- ").append(neededItem.getName())
                             .append(": Mevcut: ").append(String.format("%.2f", 
                                 currentStock != null ? currentStock.getAmount() : 0))
                             .append(" ").append(neededItem.getUnit())
                             .append(", Gerekli: ").append(String.format("%.2f", neededItem.getAmount()))
                             .append(" ").append(neededItem.getUnit())
                             .append("\n");
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Stok Uyarısı");
        alert.setHeaderText("Sipariş oluşturulamadı!");
        alert.setContentText(warningMsg.toString());
        alert.showAndWait();
    }
}