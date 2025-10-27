package com.example.prueba;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Clase simple para conectar con el webhook de n8n
 */
public class WebhookHelper {
    
    private static final String TAG = "WebhookHelper";
    // Updated to the webhook URL provided by the user
    private static final String WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6";

    private OkHttpClient client;
    
    public WebhookHelper() {
        this.client = new OkHttpClient();
    }
    
    /**
     * Envía una petición GET al webhook
     * @param listener Callback para recibir el resultado
     */
    public void sendGetRequest(final WebhookListener listener) {
        Request request = new Request.Builder()
                .url(WEBHOOK_URL)
                .get()
                .build();
        
        executeRequest(request, listener);
    }
    
    /**
     * Envía una petición POST al webhook con datos JSON
     * @param jsonData Datos en formato JSON (puede ser null para POST vacío)
     * @param listener Callback para recibir el resultado
     */
    public void sendPostRequest(String jsonData, final WebhookListener listener) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        
        // Si no hay datos, enviar JSON vacío
        if (jsonData == null || jsonData.isEmpty()) {
            jsonData = "{}";
        }
        
        RequestBody body = RequestBody.create(jsonData, JSON);
        
        Request request = new Request.Builder()
                .url(WEBHOOK_URL)
                .post(body)
                .build();
        
        executeRequest(request, listener);
    }
    
    /**
     * Ejecuta la petición HTTP en segundo plano
     */
    private void executeRequest(Request request, final WebhookListener listener) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error de conexión: " + e.getMessage());
                notifyOnMainThread(listener, false, "Error: " + e.getMessage(), 0);
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Conexión exitosa - Código: " + code);
                    notifyOnMainThread(listener, true, responseBody, code);
                } else {
                    Log.e(TAG, "Error del servidor - Código: " + code);
                    notifyOnMainThread(listener, false, "Código de error: " + code, code);
                }
                
                response.close();
            }
        });
    }
    
    /**
     * Notifica el resultado en el hilo principal (UI Thread)
     */
    private void notifyOnMainThread(final WebhookListener listener, 
                                     final boolean success, 
                                     final String message, 
                                     final int code) {
        if (listener != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (success) {
                        listener.onSuccess(message, code);
                    } else {
                        listener.onError(message, code);
                    }
                }
            });
        }
    }
    
    /**
     * Interface para recibir callbacks del webhook
     */
    public interface WebhookListener {
        void onSuccess(String response, int code);
        void onError(String error, int code);
    }
}
