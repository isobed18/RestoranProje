package org.restoranproje.db;

import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDAO {
    // Tüm sipariş hareketlerini kaydeder
    public static void logOrderHistory(Order order) { // orderların tüm durumlarını tutacak tabloyu loglama
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

    // Sadece DELIVERED siparişleri kaydeder
    public static void saveCompletedOrder(Order order) { // sadece tamamlanan orderları tutacak tabloya ekleme
        String sql = "INSERT INTO completed_orders (id, details, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, order.getId());
            pstmt.setString(2, order.getDetails());
            pstmt.setString(3, order.getStatus().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            // aynı idli order iki kez tamamlanmamalı hata verir buraya gelirse
            System.err.println("Completed order already saved: " + order.getId());
        }
    }
}
