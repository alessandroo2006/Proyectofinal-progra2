package com.example.prueba.api;

import com.example.prueba.models.EventoN8n;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interfaz de API para comunicarse con el webhook de n8n
 * Define los endpoints y métodos HTTP disponibles
 */
public interface N8nApiService {
    
    /**
     * Envía un evento al webhook de n8n
     * 
     * @param evento Objeto EventoN8n que será convertido a JSON
     * @return Call<Void> - La respuesta no contiene cuerpo (solo código de estado)
     */
    @POST("webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6")
    Call<Void> enviarEvento(@Body EventoN8n evento);
}
