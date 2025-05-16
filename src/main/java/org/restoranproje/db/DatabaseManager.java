package org.restoranproje.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:./src/main/resources/database/restoran.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static Connection getConnection() {
        try {
            return connect();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setupDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_history (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER," +
                    "details TEXT," +
                    "status TEXT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS completed_orders (" +
                    "id INTEGER PRIMARY KEY," +
                    "details TEXT," +
                    "status TEXT)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS menu_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "type TEXT," +
                    "price REAL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stock_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "amount REAL," +
                    "unit TEXT," +
                    "unit_cost REAL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS menu_item_stock (" +
                    "menu_item_id INTEGER," +
                    "stock_item_id INTEGER," +
                    "amount REAL," +  // EKLENDİ: Tarif için gerekli miktar
                    "FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)," +
                    "FOREIGN KEY (stock_item_id) REFERENCES stock_items(id)," +
                    "PRIMARY KEY (menu_item_id, stock_item_id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_items (" +
                    "order_id INTEGER," +
                    "menu_item_id INTEGER," +
                    "FOREIGN KEY (order_id) REFERENCES order_history(order_id)," +
                    "FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)," +
                    "PRIMARY KEY (order_id, menu_item_id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL)");

            System.out.println("Veritabanı kurulumu tamamlandı.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}