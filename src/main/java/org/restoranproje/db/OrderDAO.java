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
    // Tüm sipariş hareketlerini kaydeder
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
        String sql = "SELECT * FROM order_history WHERE status != 'DELIVERED' AND status != 'CANCELLEDBP' ORDER BY order_id";

        // Sipariş kalemlerini almak için kullanılacak SQL sorgusu
        String itemsSql = "SELECT oi.menu_item_id FROM order_items oi WHERE oi.order_id = ?";

        // Menü kalemesi detaylarını almak için SQL sorgusu
        String menuItemSql = "SELECT name, description, type, price FROM menu_items WHERE id = ?";

        // Menü kalemesine ait stok kalemlerini almak için SQL sorgusu
        String stockItemsSql = "SELECT mis.stock_item_id, si.name AS stock_name, si.description AS stock_desc, si.count, si.price AS stock_price " +
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

                // Sipariş kalemlerini getir
                itemsStmt.setInt(1, orderId);
                ResultSet itemsRs = itemsStmt.executeQuery();
                while (itemsRs.next()) {
                    int menuItemId = itemsRs.getInt("menu_item_id");

                    // Menü kalemi detaylarını getir
                    menuItemStmt.setInt(1, menuItemId);
                    ResultSet menuItemRs = menuItemStmt.executeQuery();
                    if (menuItemRs.next()) {
                        String menuName = menuItemRs.getString("name");
                        String menuDescription = menuItemRs.getString("description");
                        String menuTypeStr = menuItemRs.getString("type");
                        int menuPrice = menuItemRs.getInt("price");
                        MenuItemType menuItemType = MenuItemType.valueOf(menuTypeStr);
                        ArrayList<StockItem> stockItems = new ArrayList<>();

                        // Menü kalemine ait stok kalemlerini getir
                        stockItemsStmt.setInt(1, menuItemId);
                        ResultSet stockItemsRs = stockItemsStmt.executeQuery();
                        while (stockItemsRs.next()) {
                            int stockID = stockItemsRs.getInt("id");
                            String stockName = stockItemsRs.getString("stock_name");
                            String stockDescription = stockItemsRs.getString("stock_desc");
                            int stockCount = stockItemsRs.getInt("count");
                            int stockPrice = stockItemsRs.getInt("stock_price");
                            stockItems.add(new StockItem(stockID, stockName, stockDescription, stockCount, stockPrice));
                        }
                        stockItemsRs.close();

                        order.getItems().add(new MenuItem(menuName, menuDescription, menuItemType, menuPrice));
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

    // Sadece DELIVERED siparişleri kaydeder
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
}