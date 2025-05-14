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
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // order history bir orderın tüm durumlarını tutacak
            String orderHistoryTable = "CREATE TABLE IF NOT EXISTS order_history (" +
                    "order_id INTEGER," +
                    "details TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";
            // completed orders sadece tamamlananları tutacak
            String completedOrdersTable = "CREATE TABLE IF NOT EXISTS completed_orders (" +
                    "id INTEGER PRIMARY KEY," +
                    "details TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            // userları tutacak tablo
            String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL" +
                    ");";



            stmt.execute(orderHistoryTable);
            stmt.execute(completedOrdersTable);
            stmt.execute(userTable);
            System.out.println("Database setup completed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

