package org.restoranproje.db;

import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import org.restoranproje.model.MenuItem;
import org.restoranproje.model.MenuItemType;
import org.restoranproje.model.StockItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    public static void logOrderHistory(Order order) {
        String sql = "INSERT INTO order_history (order_id, details, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getId());
            pstmt.setString(2, order.getDetails());
            pstmt.setString(3, order.getStatus().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> getActiveOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM order_history WHERE status != 'DELIVERED' AND status != 'CANCELLED' ORDER BY order_id";

        String itemsSql = "SELECT oi.menu_item_id FROM order_items oi WHERE oi.order_id = ?";
        String menuItemSql = "SELECT name, description, type, price FROM menu_items WHERE id = ?";
        String stockItemsSql = "SELECT mis.stock_item_id, si.name AS stock_name, si.amount, si.unit, si.unit_cost " +
                "FROM menu_item_stock mis JOIN stock_items si ON mis.stock_item_id = si.id WHERE mis.menu_item_id = ?";

        Map<Integer, Order> orderMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:./src/main/resources/database/restoran.db");
             Statement stmt = conn.createStatement();
             PreparedStatement itemsStmt = conn.prepareStatement(itemsSql);
             PreparedStatement menuItemStmt = conn.prepareStatement(menuItemSql);
             PreparedStatement stockItemsStmt = conn.prepareStatement(stockItemsSql);
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String detail = rs.getString("details");
                String statusStr = rs.getString("status");
                OrderStatus status = OrderStatus.valueOf(statusStr);

                Order order = orderMap.get(orderId);
                if (order == null) {
                    order = new Order(orderId, detail, new ArrayList<>());
                    order.setStatus(status);
                    orderMap.put(orderId, order);
                } else {
                    order.setStatus(status);
                }

                itemsStmt.setInt(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();
                while (itemsRs.next()) {
                    int menuItemId = itemsRs.getInt("menu_item_id");

                    menuItemStmt.setInt(1, menuItemId);
                    ResultSet menuItemRs = menuItemStmt.executeQuery();
                    if (menuItemRs.next()) {
                        String menuName = menuItemRs.getString("name");
                        String menuDescription = menuItemRs.getString("description");
                        String menuTypeStr = menuItemRs.getString("type");
                        double menuPrice = menuItemRs.getDouble("price");
                        MenuItemType menuItemType = MenuItemType.valueOf(menuTypeStr);
                        ArrayList<StockItem> stockItems = new ArrayList<>();

                        stockItemsStmt.setInt(1, menuItemId);
                        ResultSet stockItemsRs = stockItemsStmt.executeQuery();
                        while (stockItemsRs.next()) {
                            String stockName = stockItemsRs.getString("stock_name");
                            double stockAmount = stockItemsRs.getDouble("amount");
                            String stockUnit = stockItemsRs.getString("unit");
                            double stockUnitCost = stockItemsRs.getDouble("unit_cost");
                            stockItems.add(new StockItem(stockName, stockAmount, stockUnit, stockUnitCost));
                        }
                        stockItemsRs.close();

                        order.getItems().add(new MenuItem(menuName, menuDescription, menuItemType, menuPrice, stockItems));
                    }
                    menuItemRs.close();
                }
                itemsRs.close();
            }

            orders.addAll(orderMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static void saveCompletedOrder(Order order) {
        String sql = "INSERT INTO completed_orders (id, details, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getId());
            pstmt.setString(2, order.getDetails());
            pstmt.setString(3, order.getStatus().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Completed order already saved: " + order.getId());
        }
    }
    public static List<Order> getFullOrderHistory() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, details, status FROM order_history ORDER BY id"; // sıralı geçmiş

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String details = rs.getString("details");
                String statusStr = rs.getString("status");

                Order order = new Order(orderId, details, new ArrayList<>());
                order.setStatus(OrderStatus.valueOf(statusStr));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}