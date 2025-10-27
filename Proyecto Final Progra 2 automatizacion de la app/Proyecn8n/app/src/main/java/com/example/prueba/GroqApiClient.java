package com.example.prueba;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroqApiClient {
    private static final String BASE_URL = "https://api.groq.com/openai/v1/";
    private static final String API_KEY = "TU_API_KEY_AQUI";
    
    private static GroqApiService apiService;
    
    public static GroqApiService getApiService() {
        if (apiService == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Create OkHttp client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            
            // Create Retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            
            apiService = retrofit.create(GroqApiService.class);
        }
        return apiService;
    }
    
    public static String getApiKey() {
        return "Bearer " + API_KEY;
    }
}
