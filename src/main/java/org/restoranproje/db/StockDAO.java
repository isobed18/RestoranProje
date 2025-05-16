package org.restoranproje.db;

import org.restoranproje.model.StockItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    private Connection connection;

    public StockDAO() {
        connection = DatabaseManager.getConnection();
    }

    public List<StockItem> getAllStockItems() {
        List<StockItem> items = new ArrayList<>();
        String query = "SELECT name, amount, unit, unit_cost FROM stock_items";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                StockItem item = new StockItem(
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    rs.getString("unit"),
                    rs.getDouble("unit_cost")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void insertStockItem(StockItem item) {
        String query = "INSERT INTO stock_items (name, amount, unit, unit_cost) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, item.getName());
            pstmt.setDouble(2, item.getAmount());
            pstmt.setString(3, item.getUnit());
            pstmt.setDouble(4, item.getUnitCost());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStockItem(StockItem item) {
        String query = "UPDATE stock_items SET amount = ?, unit = ?, unit_cost = ? WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, item.getAmount());
            pstmt.setString(2, item.getUnit());
            pstmt.setDouble(3, item.getUnitCost());
            pstmt.setString(4, item.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStockItem(String name) {
        String query = "DELETE FROM stock_items WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StockItem getStockItemByName(String name) {
        String query = "SELECT name, amount, unit, unit_cost FROM stock_items WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new StockItem(
                    rs.getString("name"),
                    rs.getDouble("amount"),
                    rs.getString("unit"),
                    rs.getDouble("unit_cost")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 