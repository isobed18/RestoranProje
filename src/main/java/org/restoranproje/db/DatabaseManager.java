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

    public static void setupDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // order_history tablosunu oluştur (zaten varsa hata vermez)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_history (" +
                    "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "details TEXT," +
                    "status TEXT)");

            // completed_orders tablosunu oluştur (zaten varsa hata vermez)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS completed_orders (" +
                    "id INTEGER PRIMARY KEY," +
                    "details TEXT," +
                    "status TEXT)");

            // menu_items tablosunu oluştur (örnek sütunlarla)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS menu_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "type TEXT," +
                    "price INTEGER)");

            // stock_items tablosunu oluştur (örnek sütunlarla)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stock_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "count INTEGER," +
                    "price INTEGER)");

            // menu_item_stock tablosunu oluştur (ilişki tablosu)
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS menu_item_stock (" +
                    "menu_item_id INTEGER," +
                    "stock_item_id INTEGER," +
                    "FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)," +
                    "FOREIGN KEY (stock_item_id) REFERENCES stock_items(id)," +
                    "PRIMARY KEY (menu_item_id, stock_item_id))");

            // order_items tablosunu oluştur
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_items (" +
                    "order_id INTEGER," +
                    "menu_item_id INTEGER," +
                    "FOREIGN KEY (order_id) REFERENCES order_history(order_id)," +
                    "FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)," +
                    "PRIMARY KEY (order_id, menu_item_id))"); // Bileşik anahtar

            System.out.println("Veritabanı kurulumu tamamlandı.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
