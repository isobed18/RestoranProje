package org.restoranproje.db;

import org.restoranproje.model.StockItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public static List<StockItem> getAllStockItems() {
        List<StockItem> list = new ArrayList<>();
        String sql = "SELECT name, description, count, price FROM stock_items";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                double count = rs.getDouble("count");
                int price = rs.getInt("price");

                list.add(new StockItem(id, name, desc, count, price));
            }

        } catch (SQLException e) {
            System.out.println("Stoklar çekilirken hata oluştu: " + e.getMessage());
        }

        return list;
    }

    public static void updateStockItem(StockItem item) {
        String sql = "UPDATE stock_items SET description=?, count=?, price=? WHERE name=?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getDescription());
            pstmt.setDouble(2, item.getCount());
            pstmt.setInt(3, item.getPrice());
            pstmt.setString(4, item.getName());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Stok güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    public static void insertStockItem(StockItem item) {
        String sql = "INSERT INTO stock_items (name, description, count, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setDouble(3, item.getCount());
            pstmt.setInt(4, item.getPrice());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Yeni stok eklenirken hata oluştu: " + e.getMessage());
        }
    }
}
