package org.restoranproje.db;

import org.restoranproje.model.MenuItem;
import org.restoranproje.model.MenuItemType;
import org.restoranproje.model.StockItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    private Connection connection;

    public MenuDAO() {
        connection = DatabaseManager.getConnection();
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String menuQuery = "SELECT id, name, description, type, price FROM menu_items";
        String stockQuery = "SELECT si.id, si.name, mis.amount, si.unit, si.unit_cost FROM menu_item_stock mis " +
                "JOIN stock_items si ON mis.stock_item_id = si.id WHERE mis.menu_item_id = ?";

        try (Statement menuStmt = connection.createStatement();
             ResultSet menuRs = menuStmt.executeQuery(menuQuery);
             PreparedStatement stockStmt = connection.prepareStatement(stockQuery)) {

            while (menuRs.next()) {
                int menuId = menuRs.getInt("id");
                String name = menuRs.getString("name");
                String desc = menuRs.getString("description");
                MenuItemType type = MenuItemType.valueOf(menuRs.getString("type"));
                double price = menuRs.getDouble("price");

                ArrayList<StockItem> stockItems = new ArrayList<>();
                stockStmt.setInt(1, menuId);
                ResultSet stockRs = stockStmt.executeQuery();

                while (stockRs.next()) {
                    int stockId = stockRs.getInt("id");
                    String stockName = stockRs.getString("name");
                    double amount = stockRs.getDouble("amount");
                    String unit = stockRs.getString("unit");
                    double unitCost = stockRs.getDouble("unit_cost");
                    StockItem stockItem = new StockItem(stockName, amount, unit, unitCost);
                    stockItem.setId(stockId);
                    stockItems.add(stockItem);
                }

                stockRs.close();
                MenuItem menuItem = new MenuItem(name, desc, type, price, stockItems);
                menuItem.setId(menuId);
                menuItems.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }

    public void insertMenuItem(MenuItem menuItem, List<Integer> stockItemIds) {
        String menuQuery = "INSERT INTO menu_items (name, description, type, price) VALUES (?, ?, ?, ?)";
        String linkQuery = "INSERT INTO menu_item_stock (menu_item_id, stock_item_id, amount) VALUES (?, ?, ?)";

        try (PreparedStatement menuStmt = connection.prepareStatement(menuQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement linkStmt = connection.prepareStatement(linkQuery)) {

            menuStmt.setString(1, menuItem.getName());
            menuStmt.setString(2, menuItem.getDescription());
            menuStmt.setString(3, menuItem.getType().toString());
            menuStmt.setDouble(4, menuItem.getPrice());
            menuStmt.executeUpdate();

            ResultSet generatedKeys = menuStmt.getGeneratedKeys();
            int generatedMenuItemId = -1;
            if (generatedKeys.next()) {
                generatedMenuItemId = generatedKeys.getInt(1);
                menuItem.setId(generatedMenuItemId);
            }

            if (generatedMenuItemId != -1) {
                for (int i = 0; i < stockItemIds.size(); i++) {
                    int stockItemId = stockItemIds.get(i);
                    StockItem ingredient = menuItem.getItems().get(i);
                    linkStmt.setInt(1, generatedMenuItemId);
                    linkStmt.setInt(2, stockItemId);
                    linkStmt.setDouble(3, ingredient.getAmount());
                    linkStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMenuItemByName(String menuItemName) {
        String deleteLinkQuery = "DELETE FROM menu_item_stock WHERE menu_item_id = (SELECT id FROM menu_items WHERE name = ?)";
        String deleteMenuQuery = "DELETE FROM menu_items WHERE name = ?";

        try (PreparedStatement linkStmt = connection.prepareStatement(deleteLinkQuery);
             PreparedStatement menuStmt = connection.prepareStatement(deleteMenuQuery)) {

            linkStmt.setString(1, menuItemName);
            linkStmt.executeUpdate();

            menuStmt.setString(1, menuItemName);
            menuStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getStockItemIdsForMenuItem(int menuItemId) {
        List<Integer> stockItemIds = new ArrayList<>();
        String query = "SELECT stock_item_id FROM menu_item_stock WHERE menu_item_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, menuItemId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stockItemIds.add(rs.getInt("stock_item_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockItemIds;
    }
} 