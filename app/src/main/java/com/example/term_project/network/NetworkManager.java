package com.example.term_project.network;

import android.os.Handler;
import android.os.Looper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {

    // Arka planda çalışacak tek bir iş parçacığı (Thread) havuzu oluşturuyoruz
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Arka plandan gelen sonucu ana ekrana (UI) güvenlice iletmek için Handler kullanıyoruz
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // İnternet sonucunu dinleyecek bir arayüz (Interface) - OOP kuralı
    public interface NetworkCallback {
        void onSuccess(String result);
        void onError(Exception e);
    }

    // İnternetten veri çeken fonksiyonumuz (Gereksinim 6)
    public void fetchDailyData(String urlString, NetworkCallback callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try { // Exception Handling - Hata Yakalama (Gereksinim 5)
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000); // 5 saniye bağlantı süresi
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();

                    // Bağlantı başarılıysa (HTTP 200 OK)
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        // Başarılı sonucu ana ekrana gönderiyoruz
                        final String finalResult = response.toString();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(finalResult);
                            }
                        });
                    } else {
                        throw new Exception("HTTP Hata Kodu: " + responseCode);
                    }

                } catch (final Exception e) {
                    // Bir hata oluşursa (Örn: İnternet yoksa) hatayı ana ekrana bildiriyoruz
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                } finally {
                    // Bağlantıları güvenli bir şekilde kapatıyoruz
                    try {
                        if (reader != null) reader.close();
                        if (connection != null) connection.disconnect();
                    } catch (Exception ignored) {}
                }
            }
        });
    }
}