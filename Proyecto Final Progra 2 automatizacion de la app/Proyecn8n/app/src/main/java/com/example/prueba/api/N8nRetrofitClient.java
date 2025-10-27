package com.example.prueba.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente Retrofit para gestionar las conexiones al webhook de n8n
 * Implementa el patrón Singleton para reutilizar la instancia
 */
public class N8nRetrofitClient {
    
    // URL base del webhook de n8n
    private static final String BASE_URL = "https://foxyti.app.n8n.cloud/";
    
    // Instancia única de Retrofit (Singleton)
    private static Retrofit retrofit = null;
    
    /**
     * Obtiene la instancia única de Retrofit
     * Si no existe, la crea con la configuración necesaria
     * 
     * @return Retrofit configurado y listo para usar
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Configurar el interceptor de logs (útil para debugging)
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Configurar el cliente HTTP con timeouts
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)           // Logs de peticiones/respuestas
                    .connectTimeout(30, TimeUnit.SECONDS)        // Timeout de conexión
                    .readTimeout(30, TimeUnit.SECONDS)           // Timeout de lectura
                    .writeTimeout(30, TimeUnit.SECONDS)          // Timeout de escritura
                    .build();
            
            // Crear la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)                           // URL base
                    .client(okHttpClient)                        // Cliente HTTP configurado
                    .addConverterFactory(GsonConverterFactory.create()) // Convertidor JSON
                    .build();
        }
        
        return retrofit;
    }
    
    /**
     * Obtiene una instancia del servicio de API
     * 
     * @return N8nApiService listo para hacer llamadas
     */
    public static N8nApiService getApiService() {
        return getClient().create(N8nApiService.class);
    }
}
