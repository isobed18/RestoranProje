package org.restoranproje.db;

import org.restoranproje.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public static List<MenuItem> getAllMenuItems(List<StockItem> stockItems) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                MenuItemType type = MenuItemType.valueOf(rs.getString("type"));
                int price = rs.getInt("price");
                FoodStatus status = FoodStatus.valueOf(rs.getString("status"));

                MenuItem item = new MenuItem(name, desc, type, price);
                item.setFoodStatus(status);

                // Tarifleri getir
                item.setRecipe(getRecipeForMenuItem(conn, id, stockItems));

                items.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Menü öğeleri çekilirken hata oluştu: " + e.getMessage());
        }

        return items;
    }

    private static List<RecipeIngredient> getRecipeForMenuItem(Connection conn, int menuItemId, List<StockItem> stockItems) {
        List<RecipeIngredient> recipe = new ArrayList<>();
        String sql = "SELECT stock_item_id, amount FROM menu_item_stock WHERE menu_item_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, menuItemId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int stockId = rs.getInt("stock_item_id");
                double amount = rs.getDouble("amount");

                for (StockItem s : stockItems) {
                    if (s.getId() == stockId) {
                        recipe.add(new RecipeIngredient(s, amount));
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Tarif yüklenirken hata: " + e.getMessage());
        }

        return recipe;
    }

    public static void insertMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu_items (name, description, type, price, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getDescription());
            pstmt.setString(3, item.getType().name());
            pstmt.setInt(4, item.getPrice());
            pstmt.setString(5, item.getFoodStatus().name());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int menuItemId = rs.getInt(1);
                insertRecipe(conn, menuItemId, item.getRecipe());
            }

        } catch (SQLException e) {
            System.out.println("Menü öğesi eklenirken hata oluştu: " + e.getMessage());
        }
    }

    private static void insertRecipe(Connection conn, int menuItemId, List<RecipeIngredient> recipe) throws SQLException {
        String sql = "INSERT INTO menu_item_stock (menu_item_id, stock_item_id, amount) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (RecipeIngredient ri : recipe) {
                pstmt.setInt(1, menuItemId);
                pstmt.setInt(2, ri.getStockItem().getId());
                pstmt.setDouble(3, ri.getAmount());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
}
