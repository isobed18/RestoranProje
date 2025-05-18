# 🍽️ Restoran Yönetim Sistemi

Modern ve kullanıcı dostu bir restoran yönetim sistemi. Bu proje, restoran işletmelerinin günlük operasyonlarını dijital ortamda yönetmelerini sağlayan kapsamlı bir çözümdür.

## 🌟 Özellikler

### 👨‍🍳 Aşçı Paneli
- Siparişleri gerçek zamanlı görüntüleme
- Sipariş durumlarını güncelleme(HAZIRLANDI)
- Sipariş hazırlama süreçlerini yönetme

### 👨‍💼 Garson Paneli
- Yeni sipariş oluşturma(Menüden ürün seçerek)
- Mevcut siparişleri görüntüleme
- Sipariş durumlarını güncelleme
- Sipariş iptal etme ve düzenleme

### 👨‍💻 Yönetici Paneli
- Menü yönetimi 
- Stok takibi ve yönetimi
- Personel yönetimi
- Sipariş geçmişi görüntüleme

### 🔐 Güvenlik
- Rol tabanlı erişim kontrolü
- Güvenli giriş sistemi
- Kullanıcı yetkilendirme

## 💻 Teknik Detaylar

### Kullanılan Teknolojiler
- Java
- JavaFX
- SQLite
- JDBC

### Veritabanı Yapısı
- Menü öğeleri
- Stok takibi
- Sipariş geçmişi
- Personel yönetimi
- Tamamlanan sipariş takibi
  

## 🚀 Kurulum

1. Projeyi klonlayın:
```bash
git clone https://github.com/isobed18/RestoranProje
```

2. Gerekli bağımlılıkları yükleyin:
```bash
mvn install
```

3. Veritabanını oluşturun:
```bash
src/main/resources/database/restoran.db
```
4. Maini başlatıp Veritabanını doldurun:
   ``` Main.Java çalıştırın(Database setup, temel kullanıcılar, menü öğeleri eklemek için)
   src/main/java/Main.java
   ```
5. IntelliJ IDEA'da Projeyi Çalıştırma:
- Projeyi IntelliJ IDEA'da açın
- Sol üstteki MainMenu kısmından Run bölümünde  "Edit Configurations"a tıklayın
- "+" butonuna(Add New Configuration) tıklayıp "Application" seçin
- Aşağıdaki ayarları yapın:

  - Name: `RestaurantApp`
  - Main class: `org.restoranproje.gui.RestaurantApp`
  - VM options: `--module-path [JavaFX-SDK-lib-yolu] --add-modules javafx.controls,javafx.fxml,javafx.graphics`
    - Örnek: `--module-path C:\Program Files\JavaFX\lib --add-modules javafx.controls,javafx.fxml,javafx.graphics`
    - Not: [JavaFX-SDK-lib-yolu] kısmını kendi bilgisayarınızda JavaFX'i çıkarttığınız konumdaki lib klasörünün yolu ile değiştirin

-RestuarantApp.java'yı çalıştırın login ekranında Veritabanına kaydedilmiş Userlardan biriyle giriş yapabilirsiniz(Userlar Main.java'da eklendi) 

## 📋 Sistem Gereksinimleri
- Java 23 veya üzeri
- Maven
- SQLite
- JavaFX SDK
- IntelliJ IDEA (önerilen)


## 🔜 Gelecek Özellikler
- Finansal raporlama sistemi


## 👥 Katkıda Bulunma
1. Bu depoyu fork edin
2. Yeni bir branch oluşturun (`git checkout -b feature/yeniOzellik`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni özellik: Açıklama'`)
4. Branch'inizi push edin (`git push origin feature/yeniOzellik`)
5. Pull Request oluşturun


## 📝 Lisans
Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.



### Proje Geliştiricileri
- **İshak Bedir Yorgancı**  
  Github: [https://github.com/isobed18](https://github.com/isobed18)  
  Mail: ishakbediryorganci@gmail.com

- **Ahmet Emir Coşkun**  
  Github: [https://github.com/ahmetemircoskun](https://github.com/ahmetemircoskun)  
  Mail: aecoskuno@gmail.com

- **Yusuf Duman**  
  Github: [https://github.com/yusufduman78](https://github.com/yusufduman78)  
  Mail: yusuf78duman@gmail.com

### Proje Linki
[https://github.com/isobed18/RestoranProje](https://github.com/isobed18/RestoranProje) 


