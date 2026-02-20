# 🚀 Softmax Action Selection Tabanlı Akıllı Yük Dengeleyici (Load Balancer)

Bu proje, **Kırklareli Üniversitesi Bilgisayar Mühendisliği** bölümü ödevi kapsamında geliştirilmiştir. Dağıtık sistemlerde, sunucu performanslarının sabit olmadığı (non-stationary) ve gürültülü verilerin bulunduğu senaryolarda, toplam gecikme süresini (latency) minimize eden olasılıksal bir yük dengeleme simülasyonudur.

---

## 📌 Proje Hakkında

Geleneksel yük dengeleme yöntemleri (Round-Robin, Random vb.), sunucuların anlık yükünü veya değişen kapasitelerini dikkate almazlar. Bu projede, **Multi-Armed Bandit (MAB)** problemlerinde kullanılan **Softmax Action Selection** algoritması uygulanarak "Öğrenen bir Yük Dengeleyici" tasarlanmıştır.

### Temel Özellikler
* **Dinamik Öğrenme:** Sunucuların başarı oranlarını (ödül/reward) çalışma zamanında izler ve tahminlerde bulunur.
* **Keşif ve İstismar (Exploration vs. Exploitation):** Sıcaklık parametresi ($\tau = 0.5$) kullanılarak, en iyi sunucudan faydalanma ile yeni durumları test etme arasında denge kurulmuştur.
* **Nümerik Stabilite:** Üstel ($e^x$) hesaplamalarda oluşabilecek `Infinity` (taşma) hatalarını önlemek için **Max-Normalization** tekniği entegre edilmiştir.

---

## 🛠️ Teknik Analiz

### 1. Algoritma Mantığı
Softmax, her sunucunun geçmiş performansına dayalı bir olasılık dağılımı oluşturur:
$$P(i) = \frac{e^{Q_i / \tau}}{\sum_{j=1}^{K} e^{Q_j / \tau}}$$
Burada $Q_i$, sunucunun tahmini performansını, $\tau$ ise seçimdeki rastgelelik düzeyini temsil eder.

### 2. Karmaşıklık (Big O) Analizi
* **Zaman Karmaşıklığı:** $O(K)$ – Burada $K$ sunucu sayısıdır. Her seçim işlemi sunucu sayısına bağlı doğrusal sürede tamamlanır.
* **Alan (Memory) Karmaşıklığı:** $O(K)$ – Sadece sunucu istatistiklerini tutan diziler kullanıldığı için bellek kullanımı sunucu sayısıyla doğru orantılıdır.

---

## 💻 Kurulum ve Çalıştırma

Proje standart Java kütüphanelerini kullanır, harici bir bağımlılık gerektirmez.

1.  **Repoyu Klonlayın:**
    ```bash
    git clone [https://github.com/kullaniciadi/softmax-load-balancer.git](https://github.com/kullaniciadi/softmax-load-balancer.git)
    ```
2.  **Kaynak Klasörüne Gidin:**
    ```bash
    cd softmax-load-balancer/src
    ```
3.  **Derleyin ve Çalıştırın:**
    ```bash
    javac SoftmaxLoadBalancer.java
    java SoftmaxLoadBalancer
    ```

---

## 📊 Örnek Simülasyon Çıktısı

```text
--- Final Raporu ---
Sunucu 0: Seçilme Sayısı = 395, Tahmini Performans = 0,91 (Gerçek: 0,90)
Sunucu 1: Seçilme Sayısı = 225, Tahmini Performans = 0,67 (Gerçek: 0,70)
Sunucu 2: Seçilme Sayısı = 163, Tahmini Performans = 0,47 (Gerçek: 0,50)
...
