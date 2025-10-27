package com.example.prueba;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class N8nWebhookClient {
    private static final String TAG = "N8nWebhookClient";
    // Updated base and webhook URL to point to the user's n8n webhook (foxyti)
    private static final String BASE_URL = "https://foxyti.app.n8n.cloud/";
    private static final String FINANCIAL_DATA_WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6";

    private static N8nWebhookClient instance;
    private N8nWebhookService webhookService;

    private N8nWebhookClient() {
        initializeService();
    }

    public static synchronized N8nWebhookClient getInstance() {
        if (instance == null) {
            instance = new N8nWebhookClient();
        }
        return instance;
    }

    private void initializeService() {
        try {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            webhookService = retrofit.create(N8nWebhookService.class);
            Log.d(TAG, "N8n webhook service initialized successfully");

        } catch (Exception e) {
            Log.e(TAG, "Error initializing N8n webhook service", e);
        }
    }

    /**
     * Generic method to send data to any n8n webhook URL.
     * @param fullUrl The full URL of the webhook.
     * @param action A string describing the action (e.g., \"financial_tools_clicked\").
     * @param data The data payload to send.
     * @param userId The ID of the user triggering the event.
     * @param callback The callback to handle success or error.
     */
    public void sendEventToWebhook(String fullUrl, String action, Object data, String userId, WebhookCallback callback) {
        if (webhookService == null) {
            Log.e(TAG, "Webhook service not initialized");
            if (callback != null) {
                callback.onError("Service not initialized");
            }
            return;
        }

        try {
            N8nWebhookService.N8nRequest request = new N8nWebhookService.N8nRequest(action, data, userId);
            Call<N8nWebhookService.N8nResponse> call = webhookService.sendData(fullUrl, request);

            call.enqueue(new Callback<N8nWebhookService.N8nResponse>() {
                @Override
                public void onResponse(Call<N8nWebhookService.N8nResponse> call, Response<N8nWebhookService.N8nResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Webhook call to " + fullUrl + " successful. Response: " + response.body().getMessage());
                        if (callback != null) {
                            callback.onSuccess(response.body());
                        }
                    } else {
                        String errorMsg = "HTTP Error: " + response.code();
                        Log.e(TAG, "Webhook call to " + fullUrl + " failed. " + errorMsg);
                        if (callback != null) {
                            callback.onError(errorMsg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<N8nWebhookService.N8nResponse> call, Throwable t) {
                    String errorMsg = "Network error: " + t.getMessage();
                    Log.e(TAG, "Webhook call to " + fullUrl + " failed. " + errorMsg, t);
                    if (callback != null) {
                        callback.onError(errorMsg);
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error sending data to webhook " + fullUrl, e);
            if (callback != null) {
                callback.onError("Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Sends financial data to the original, specific webhook.
     * This method is kept for backward compatibility.
     */
    public void sendFinancialData(String userId, Object financialData, WebhookCallback callback) {
        sendEventToWebhook(
            FINANCIAL_DATA_WEBHOOK_URL,
            "financial_data_update",
            financialData,
            userId,
            callback
        );
    }

    /**
     * Tests the connection to the original webhook.
     * This method is kept for backward compatibility.
     */
    public void testConnection(WebhookCallback callback) {
        sendEventToWebhook(
            FINANCIAL_DATA_WEBHOOK_URL,
            "test_connection",
            "Testing connection from Android app",
            "test_user",
            callback
        );
    }

    // Interface for handling webhook responses
    public interface WebhookCallback {
        void onSuccess(N8nWebhookService.N8nResponse response);
        void onError(String error);
    }
}
