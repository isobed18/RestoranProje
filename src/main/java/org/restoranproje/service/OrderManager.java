package org.restoranproje.service;

import org.restoranproje.db.OrderDAO;
import org.restoranproje.model.*;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<Observer> observers = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private StockManager smanager = new StockManager();

    public OrderManager() {
        // Uygulama başladığında veya OrderManager oluşturulduğunda aktif siparişleri
        // yükle
        loadActiveOrders();
    }

    private void loadActiveOrders() {
        List<Order> activeOrdersFromDB = OrderDAO.getActiveOrders();
        if (activeOrdersFromDB != null) {
            this.orders.addAll(activeOrdersFromDB);
            System.out.println("Veritabanından " + activeOrdersFromDB.size() + " aktif sipariş yüklendi.");
        } else {
            System.out.println("Veritabanından aktif sipariş yüklenemedi veya hiç aktif sipariş yok.");
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
        smanager.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
        smanager.removeObserver(observer);
    }

    public StockManager getStockManager() {
        return smanager;
    }

    public void notifyObservers(Order order) {
        for (Observer obs : observers) {
            obs.update(order);
        }
    }

    public Order findOrderById(int id) {
        for (Order o : orders) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        if (smanager.canFulfillOrder(order)) {
            smanager.fulfillOrder(order);
            orders.add(order);
            OrderDAO.logOrderHistory(order);
            notifyObservers(order);
        } else {
            System.out.println("Stok yetersiz! Sipariş alınamadı.");
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = findOrderById(orderId);

        if (order != null) {
            order.setStatus(newStatus);
            OrderDAO.logOrderHistory(order);
            if (newStatus == OrderStatus.DELIVERED) {
                OrderDAO.saveCompletedOrder(order);
            }
            if (newStatus == OrderStatus.CANCELLEDBP) {
                smanager.restoreStockForOrder(order);
            }
            notifyObservers(order);
            System.out.println("Durum güncellendi: " + order); // Durum güncellendi mesajını buraya taşıdım
        } else {
            System.out.println("Order not found");
        }

        if (newStatus == OrderStatus.COMPLETED) {
            reduceStockIfOrderCompleted(order);
        }

    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == status) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    public void reduceStockIfOrderCompleted(Order order) {
        if (order.getStatus() != OrderStatus.COMPLETED)
            return;

        for (MenuItem item : order.getItems()) {
            boolean enough = true;
            for (RecipeIngredient ri : item.getRecipe()) {
                if (ri.getStockItem().getCount() < ri.getAmount()) {
                    enough = false;
                    break;
                }
            }

            if (enough) {
                for (RecipeIngredient ri : item.getRecipe()) {
                    StockItem si = ri.getStockItem();
                    double newQty = si.getCount() - ri.getAmount();
                    si.setCount(newQty);
                }
                item.setFoodStatus(FoodStatus.AVAILABLE);
            } else {
                item.setFoodStatus(FoodStatus.OUT_OF_STOCK);
                System.out.println("UYARI: " + item.getName()
                        + " adlı ürün için stok yetersiz, sipariş tamamlandıktan sonra devre dışı bırakıldı.");
            }
        }
    }

    public boolean isMenuItemAvailable(MenuItem item) {
        if (item.getFoodStatus() == FoodStatus.OUT_OF_STOCK)
            return false;

        for (RecipeIngredient ri : item.getRecipe()) {
            if (ri.getStockItem().getCount() < ri.getAmount()) {
                return false;
            }
        }

        return true;
    }

}