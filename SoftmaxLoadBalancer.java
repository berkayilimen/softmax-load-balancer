import java.util.Arrays;
import java.util.Random;

public class SoftmaxLoadBalancer {

    // Sunucu sayısı (K)
    private static final int K = 5;
    // Sıcaklık parametresi (Tau) - Keşif ve istismar dengesi
    private static final double TAU = 0.5;
    private static final int TOTAL_REQUESTS = 1000;

    public static void main(String[] args) {
        double[] estimatedRewards = new double[K]; // Sunucuların tahmin edilen başarıları
        int[] selectionCounts = new int[K];        // Sunucuların seçilme sayıları

        // Gerçek dünya senaryosu: Her sunucunun gizli bir başarı oranı (ödül) var
        double[] truePerformance = {0.9, 0.7, 0.5, 0.3, 0.2};
        Random random = new Random();

        System.out.println("--- Softmax Yük Dengeleme Simülasyonu Başlıyor ---");

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            // 1. Softmax ile sunucu seçimi
            int selectedServer = selectServerSoftmax(estimatedRewards, TAU);

            // 2. Seçilen sunucudan yanıt al (Gürültülü veri: Başarı 1, Başarısızlık 0)
            double reward = (random.nextDouble() < truePerformance[selectedServer]) ? 1.0 : 0.0;

            // 3. Tahmini güncelle (Incremental Mean Update)
            selectionCounts[selectedServer]++;
            estimatedRewards[selectedServer] += (reward - estimatedRewards[selectedServer]) / selectionCounts[selectedServer];

            if (i % 200 == 0) {
                System.out.printf("İstek %d | Seçilen Sunucu: %d | Güncel Tahminler: %s%n",
                        i, selectedServer, Arrays.toString(estimatedRewards));
            }
        }

        System.out.println("\n--- Final Raporu ---");
        for (int i = 0; i < K; i++) {
            System.out.printf("Sunucu %d: Seçilme Sayısı = %d, Tahmini Performans = %.2f%n",
                    i, selectionCounts[i], estimatedRewards[i]);
        }
    }

    /**
     * Softmax Action Selection Algoritması
     * Olasılık P(i) = exp(Q(i)/tau) / sum(exp(Q(j)/tau))
     */
    private static int selectServerSoftmax(double[] estimates, double tau) {
        double[] probabilities = new double[K];
        double sumExp = 0.0;

        // Nümerik Stabilite için Max Değeri Bulma (Overflow önleme)
        double maxEstimate = Arrays.stream(estimates).max().getAsDouble();

        // Üstel değerleri hesapla
        for (int i = 0; i < K; i++) {
            probabilities[i] = Math.exp((estimates[i] - maxEstimate) / tau);
            sumExp += probabilities[i];
        }

        // Olasılık dağılımı üzerinden rastgele seçim yap
        double rand = new Random().nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < K; i++) {
            probabilities[i] /= sumExp;
            cumulativeProbability += probabilities[i];
            if (rand <= cumulativeProbability) {
                return i;
            }
        }
        return K - 1;
    }
}
