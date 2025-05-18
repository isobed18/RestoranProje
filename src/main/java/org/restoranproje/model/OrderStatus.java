package org.restoranproje.model;

public enum OrderStatus {
    NEW,        // sipariş alındı, chefe gitti
    COMPLETED,  // şef hazırladı şuan hazır
    DELIVERED,// müşteriye teslim edildi
    CANCELLED, // sipariş iptal edildi, stoktan düşmemeli
}
