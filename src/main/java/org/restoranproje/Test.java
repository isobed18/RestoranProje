package org.restoranproje;

import org.restoranproje.db.*;
import org.restoranproje.model.*;
import org.restoranproje.service.*;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {

        // 1. Veritabanını kur
        DatabaseManager.setupDatabase();

        // 2. Stokları oluştur
        StockItem et = new StockItem(0, "Kıyma", "Dana kıyma", 5.0, 500); // 5kg
        StockItem pirinc = new StockItem(0, "Pirinç", "Baldo pirinç", 2.0, 100);
        StockDAO.insertStockItem(et);
        StockDAO.insertStockItem(pirinc);

        List<StockItem> stockList = StockDAO.getAllStockItems();

        // 3. Tarif tanımla (örnek: Adana Kebap için)
        StockItem stokKiyma = findStockByName(stockList, "Kıyma");
        StockItem stokPirinc = findStockByName(stockList, "Pirinç");

        List<RecipeIngredient> recipe = new ArrayList<>();
        recipe.add(new RecipeIngredient(stokKiyma, 1.0)); // 1kg kıyma
        recipe.add(new RecipeIngredient(stokPirinc, 0.5)); // 0.5kg pirinç

        // 4. Menü öğesi oluştur ve kaydet
        MenuItem kebap = new MenuItem("Adana Kebap", "Bol acılı", MenuItemType.DISH, 120);
        kebap.setRecipe(recipe);
        kebap.setFoodStatus(FoodStatus.AVAILABLE);
        MenuDAO.insertMenuItem(kebap);

        // 5. Sipariş oluştur
        List<MenuItem> menuList = MenuDAO.getAllMenuItems(stockList);
        OrderManager orderManager = new OrderManager();
        StockManager stockManager = orderManager.getStockManager();

        Waiter waiter = new Waiter("Ahmet", "123");
        orderManager.addObserver(waiter);

        ArrayList<MenuItem> siparisListesi = new ArrayList<>();
        siparisListesi.add(menuList.get(0)); // Adana Kebap

        Order siparis = new Order(1, "Masalar 5 için", siparisListesi);
        boolean alinabilir = orderManager.isMenuItemAvailable(menuList.get(0));

        if (alinabilir) {
            orderManager.addOrder(siparis);
            orderManager.updateOrderStatus(1, OrderStatus.COMPLETED);
            System.out.println("Sipariş başarıyla alındı ve tamamlandı.");
        } else {
            System.out.println("Bu sipariş verilemez: stok yetersiz.");
        }

        // 6. Siparişi bir kez daha dene (stok düşmüş olmalı)
        Order ikinci = new Order(2, "Masalar 2 için", siparisListesi);
        if (orderManager.isMenuItemAvailable(menuList.get(0))) {
            orderManager.addOrder(ikinci);
        } else {
            System.out.println("İkinci sipariş verilemedi: stok bitti.");
        }

        // 7. Mevcut stokları yazdır
        System.out.println("\n🧾 Kalan Stoklar:");
        stockManager.printAllStock();
    }

    private static StockItem findStockByName(List<StockItem> list, String name) {
        for (StockItem s : list) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
}
