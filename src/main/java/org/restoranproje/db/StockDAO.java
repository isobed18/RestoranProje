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
        List<StockItem> stockItems = new ArrayList<>();
        String query = "SELECT id, name, amount, unit, unit_cost FROM stock_items";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double amount = rs.getDouble("amount");
                String unit = rs.getString("unit");
                double unitCost = rs.getDouble("unit_cost");

                StockItem item = new StockItem(id, name, amount, unit, unitCost);
                stockItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockItems;
    }

    public void insertStockItem(StockItem item) {
        String query = "INSERT INTO stock_items (name, amount, unit, unit_cost) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getAmount());
            stmt.setString(3, item.getUnit());
            stmt.setDouble(4, item.getUnitCost());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                item.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStockItem(StockItem item) {
        String query = "UPDATE stock_items SET amount = ?, unit = ?, unit_cost = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, item.getAmount());
            stmt.setString(2, item.getUnit());
            stmt.setDouble(3, item.getUnitCost());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeStockItem(String name) {
        String query = "DELETE FROM stock_items WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeUnitCost(String name, double newUnitCost) {
        String query = "UPDATE stock_items SET unit_cost = ? WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newUnitCost);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StockItem getStockItemById(int id) {
        String query = "SELECT id, name, amount, unit, unit_cost FROM stock_items WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double amount = rs.getDouble("amount");
                String unit = rs.getString("unit");
                double unitCost = rs.getDouble("unit_cost");

                return new StockItem(id, name, amount, unit, unitCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public StockItem getStockItemByName(String name) {
        String query = "SELECT id, name, amount, unit, unit_cost FROM stock_items WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                double amount = rs.getDouble("amount");
                String unit = rs.getString("unit");
                double unitCost = rs.getDouble("unit_cost");

                return new StockItem(id, name, amount, unit, unitCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
} 