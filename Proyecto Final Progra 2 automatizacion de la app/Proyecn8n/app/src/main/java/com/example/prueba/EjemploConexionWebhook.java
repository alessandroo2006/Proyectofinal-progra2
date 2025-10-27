package com.example.prueba;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * EJEMPLO DE USO: Cómo conectar al webhook de n8n
 * 
 * Puedes copiar este código en cualquier Activity o Fragment
 */
public class EjemploConexionWebhook {
    
    private static final String TAG = "EjemploWebhook";
    
    /**
     * EJEMPLO 1: Enviar petición GET
     * Usa este método desde onCreate() o cualquier botón
     */
    public static void ejemplo_GET(AppCompatActivity activity) {
        WebhookHelper webhook = new WebhookHelper();
        
        webhook.sendGetRequest(new WebhookHelper.WebhookListener() {
            @Override
            public void onSuccess(String response, int code) {
                Log.d(TAG, "✓ CONEXIÓN EXITOSA");
                Log.d(TAG, "Código: " + code);
                Log.d(TAG, "Respuesta: " + response);
                
                // Mostrar mensaje al usuario
                Toast.makeText(activity, "Webhook conectado exitosamente", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(String error, int code) {
                Log.e(TAG, "✗ ERROR DE CONEXIÓN");
                Log.e(TAG, "Código: " + code);
                Log.e(TAG, "Error: " + error);
                
                // Mostrar error al usuario
                Toast.makeText(activity, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * EJEMPLO 2: Enviar petición POST con datos
     */
    public static void ejemplo_POST(AppCompatActivity activity) {
        WebhookHelper webhook = new WebhookHelper();
        
        // Datos a enviar (opcional)
        String datosJSON = "{\"mensaje\": \"Hola desde Android\", \"usuario\": \"test\"}";
        
        webhook.sendPostRequest(datosJSON, new WebhookHelper.WebhookListener() {
            @Override
            public void onSuccess(String response, int code) {
                Log.d(TAG, "✓ POST EXITOSO");
                Log.d(TAG, "Código: " + code);
                Log.d(TAG, "Respuesta: " + response);
                
                Toast.makeText(activity, "Datos enviados correctamente", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(String error, int code) {
                Log.e(TAG, "✗ ERROR EN POST");
                Log.e(TAG, "Código: " + code);
                Log.e(TAG, "Error: " + error);
                
                Toast.makeText(activity, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * EJEMPLO 3: POST vacío (sin datos)
     */
    public static void ejemplo_POST_vacio(AppCompatActivity activity) {
        WebhookHelper webhook = new WebhookHelper();
        
        webhook.sendPostRequest(null, new WebhookHelper.WebhookListener() {
            @Override
            public void onSuccess(String response, int code) {
                Log.d(TAG, "✓ Webhook conectado - Código " + code);
                Toast.makeText(activity, "Conexión establecida", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onError(String error, int code) {
                Log.e(TAG, "✗ Error - " + error);
                Toast.makeText(activity, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * CÓMO USAR EN TU ACTIVITY:
     * 
     * En el método onCreate() o en un botón, simplemente llama:
     * 
     *     EjemploConexionWebhook.ejemplo_GET(this);
     * 
     * O si prefieres POST:
     * 
     *     EjemploConexionWebhook.ejemplo_POST(this);
     * 
     * La petición se ejecuta automáticamente en segundo plano
     * y el resultado se muestra en el hilo principal.
     */
}

