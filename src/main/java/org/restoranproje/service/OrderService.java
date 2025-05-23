package org.restoranproje.service;

import org.restoranproje.model.Order;
import org.restoranproje.model.OrderStatus;
import org.restoranproje.db.OrderDAO;
import java.util.List;

/**
 * Sipariş işlemlerini yöneten servis sınıfı (iş mantığı burada)
 * Singleton tasarım desenini kullanır.
 */
public class OrderService {
    private static OrderService instance;

    private OrderService() {
        // Private constructor: dışarıdan nesne oluşturulamaz
    }
    /**
     * Singleton instance'ı döner.
     * Eğer daha önce oluşturulmadıysa yeni bir tane oluşturur.
     */
    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }
    /**
     * Aktif (tamamlanmamış) tüm siparişleri getirir.
     */
    public List<Order> getAllOrders() {
        return OrderDAO.getActiveOrders();
    }
    /**
     * Sipariş güncelleme işlemi yapar:
     * - Siparişi geçmişe kaydeder.
     * - Eğer sipariş tamamlandıysa "completed_orders" tablosuna ekler.
     */
    public void updateOrder(Order order) {
        // Sipariş geçmişine kaydet
        OrderDAO.logOrderHistory(order);

        // Eğer sipariş tamamlandıysa completed_orders tablosuna ekle
        if (order.getStatus() == OrderStatus.COMPLETED) {
            OrderDAO.saveCompletedOrder(order);
        }
    }

    public List<Order> getOrderHistory() {
        return OrderDAO.getFullOrderHistory();
    }
} 