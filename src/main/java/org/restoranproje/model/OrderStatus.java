package org.restoranproje.model;

public enum OrderStatus {
    NEW,        // şuan aldı
    WAITING,    // bekliyor (sırada, iletildi ama daha şef hazırlamaya geçmedi)
    PREPARING,  // şef hazırlamaya başladı
    COMPLETED,  // şef hazırladı şuan hazır
    DELIVERED,// müşteriye teslim edildi
    CANCELLEDBP, // canceled before preparing ( yani stock item yenilenmeli)
    CANCELLEDAP // canceled after preparing (stock yenilenmemeli şef yaptı yemepi)
}
