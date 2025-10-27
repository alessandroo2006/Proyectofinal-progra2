package com.example.prueba;

import android.os.Handler;
import android.os.Looper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository {
    private GroqApiService apiService;
    
    public ChatRepository() {
        this.apiService = GroqApiClient.getApiService();
    }
    
    public interface ChatCallback {
        void onSuccess(String response);
        void onError(String error);
    }
    
    public void sendMessage(String userMessage, ChatCallback callback) {
        sendMessageWithModel(userMessage, ModelManager.getCurrentModel(), callback);
    }
    
    private void sendMessageWithModel(String userMessage, String model, ChatCallback callback) {
        // Create messages array
        GroqRequest.Message[] messages = {
            new GroqRequest.Message("system", "Eres un coach financiero experto. Ayuda al usuario con consejos financieros prácticos y útiles. Responde de manera clara y concisa."),
            new GroqRequest.Message("user", userMessage)
        };
        
        // Create request
        GroqRequest request = new GroqRequest(
            model,
            messages,
            500, // max tokens
            0.7  // temperature
        );
        
        // Make API call
        Call<GroqResponse> call = apiService.createChatCompletion(
            GroqApiClient.getApiKey(),
            "application/json",
            request
        );
        
        call.enqueue(new Callback<GroqResponse>() {
            @Override
            public void onResponse(Call<GroqResponse> call, Response<GroqResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroqResponse groqResponse = response.body();
                    if (groqResponse.getChoices() != null && groqResponse.getChoices().length > 0) {
                        String aiResponse = groqResponse.getChoices()[0].getMessage().getContent();
                        // Run on main thread
                        new Handler(Looper.getMainLooper()).post(() -> {
                            callback.onSuccess(aiResponse);
                        });
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            callback.onError("No se recibió respuesta del asistente");
                        });
                    }
                } else {
                    // Check if it's a model decommissioned error
                    String errorBody = "";
                    if (response.errorBody() != null) {
                        try {
                            errorBody = response.errorBody().string();
                        } catch (Exception e) {
                            errorBody = "Error al leer respuesta";
                        }
                    }
                    
                    // If model is decommissioned, try next model
                    if (errorBody.contains("model_decommissioned") || errorBody.contains("decommissioned")) {
                        String nextModel = ModelManager.getNextModel();
                        if (!nextModel.equals(ModelManager.getCurrentModel())) {
                            // Try with next model
                            sendMessageWithModel(userMessage, nextModel, callback);
                            return;
                        }
                    }
                    
                    String errorMsg = "Error en la API: " + response.code() + " - " + errorBody;
                    final String finalErrorMsg = errorMsg;
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.onError(finalErrorMsg);
                    });
                }
            }
            
            @Override
            public void onFailure(Call<GroqResponse> call, Throwable t) {
                String errorMsg = "Error de conexión: " + t.getMessage();
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onError(errorMsg);
                });
            }
        });
    }
}
