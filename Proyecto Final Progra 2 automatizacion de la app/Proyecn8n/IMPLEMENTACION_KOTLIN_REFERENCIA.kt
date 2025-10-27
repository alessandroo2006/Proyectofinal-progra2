// ═══════════════════════════════════════════════════════════════
// 📘 IMPLEMENTACIÓN EN KOTLIN (REFERENCIA)
// ═══════════════════════════════════════════════════════════════
// 
// Este archivo es SOLO para referencia. Tu app está en Java.
// Si quieres migrar a Kotlin en el futuro, usa este código.
//
// ═══════════════════════════════════════════════════════════════

package com.example.prueba

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// ═══════════════════════════════════════════════════════════════
// 1. MODELO DE DATOS (Data Class)
// ═══════════════════════════════════════════════════════════════

/**
 * Data class que representa el evento a enviar al webhook
 * Kotlin genera automáticamente: equals(), hashCode(), toString(), copy()
 */
data class EventoN8n(
    @SerializedName("evento")
    val evento: String,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @SerializedName("usuario_id")
    val usuarioId: String? = null
)

// ═══════════════════════════════════════════════════════════════
// 2. INTERFAZ DE API (Retrofit)
// ═══════════════════════════════════════════════════════════════

/**
 * Interfaz que define los endpoints del API de n8n
 */
interface N8nApiService {
    
    /**
     * Endpoint para enviar eventos al webhook
     */
    @POST("webhook-test/a287f99f-f2df-45c8-b82a-cc1a1afd0f81")
    fun enviarEvento(@Body evento: EventoN8n): Call<Void>
}

// ═══════════════════════════════════════════════════════════════
// 3. CLIENTE RETROFIT (Object Singleton)
// ═══════════════════════════════════════════════════════════════

/**
 * Object singleton para gestionar la instancia de Retrofit
 * En Kotlin, 'object' es automáticamente un singleton thread-safe
 */
object N8nRetrofitClient {
    
    private const val BASE_URL = "https://foxyti.app.n8n.cloud/"
    
    /**
     * Instancia lazy de Retrofit (se crea solo cuando se usa)
     */
    private val retrofit: Retrofit by lazy {
        // Configurar interceptor de logs
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Configurar cliente HTTP
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        // Crear Retrofit
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Obtener instancia del servicio API
     */
    val apiService: N8nApiService by lazy {
        retrofit.create(N8nApiService::class.java)
    }
}

// ═══════════════════════════════════════════════════════════════
// 4. ACTIVITY DE HERRAMIENTAS FINANCIERAS
// ═══════════════════════════════════════════════════════════════

/**
 * Activity principal de Herramientas Financieras
 * Envía una notificación al webhook de n8n cuando se abre
 */
class HerramientasFinancierasActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "FinancialTools"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_tools)
        
        // Inicializar vistas (tu código existente)
        initializeViews()
        
        // Configurar listeners (tu código existente)
        setupClickListeners()
        
        // ⭐ ENVIAR EVENTO AL WEBHOOK DE N8N
        enviarEventoAWebhook()
    }
    
    /**
     * ⭐ MÉTODO PRINCIPAL: Envía evento al webhook usando Retrofit
     * Se ejecuta automáticamente en onCreate()
     */
    private fun enviarEventoAWebhook() {
        try {
            // Obtener ID del usuario (opcional)
            val usuarioId = try {
                SessionManager(this).userId.toString()
            } catch (e: Exception) {
                Log.w(TAG, "No se pudo obtener userId, continuando sin él")
                null
            }
            
            // Crear objeto EventoN8n
            val evento = EventoN8n(
                evento = "usuario_ingreso_a_herramientas",
                usuarioId = usuarioId
            )
            
            Log.d(TAG, "Enviando evento al webhook: $evento")
            
            // Obtener servicio API
            val apiService = N8nRetrofitClient.apiService
            
            // Crear la llamada
            val call = apiService.enviarEvento(evento)
            
            // Ejecutar de forma asíncrona
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    // ✅ RESPUESTA EXITOSA
                    if (response.isSuccessful) {
                        Log.d(TAG, "✅ Evento enviado exitosamente al webhook de n8n")
                        Log.d(TAG, "Código de respuesta: ${response.code()}")
                        
                        // Opcional: Mostrar Toast (comentado)
                        // runOnUiThread {
                        //     Toast.makeText(
                        //         this@HerramientasFinancierasActivity,
                        //         "Evento registrado",
                        //         Toast.LENGTH_SHORT
                        //     ).show()
                        // }
                    } else {
                        // ⚠️ RESPUESTA CON ERROR HTTP
                        Log.w(TAG, "⚠️ Respuesta del webhook con error: ${response.code()}")
                        Log.w(TAG, "Mensaje: ${response.message()}")
                    }
                }
                
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // ❌ ERROR DE RED
                    Log.e(TAG, "❌ Error al enviar evento al webhook", t)
                    Log.e(TAG, "Mensaje de error: ${t.message}")
                    
                    // La app continúa funcionando normalmente
                }
            })
            
        } catch (e: Exception) {
            // ❌ ERROR INESPERADO
            Log.e(TAG, "Error inesperado al intentar enviar evento", e)
        }
    }
    
    // Métodos auxiliares (tu código existente)
    private fun initializeViews() {
        // Tu código de inicialización
    }
    
    private fun setupClickListeners() {
        // Tu código de listeners
    }
}

// ═══════════════════════════════════════════════════════════════
// VENTAJAS DE KOTLIN VS JAVA
// ═══════════════════════════════════════════════════════════════
//
// 1. Data Classes:
//    - Java: 60+ líneas (getters, setters, toString, etc.)
//    - Kotlin: 5 líneas ✅
//
// 2. Null Safety:
//    - Java: if (obj != null) { ... }
//    - Kotlin: obj?.method() ✅
//
// 3. String Templates:
//    - Java: "Código: " + response.code()
//    - Kotlin: "Código: ${response.code()}" ✅
//
// 4. Singletons:
//    - Java: ~20 líneas de patrón singleton
//    - Kotlin: object MiSingleton { } ✅
//
// 5. Lambdas más limpias:
//    - Java: new Callback() { @Override ... }
//    - Kotlin: { param -> code } ✅
//
// 6. Smart Casts:
//    - Java: if (obj instanceof Type) { ((Type)obj).method(); }
//    - Kotlin: if (obj is Type) { obj.method() } ✅
//
// ═══════════════════════════════════════════════════════════════

/*
 * PARA MIGRAR DE JAVA A KOTLIN:
 * 
 * 1. Instalar el plugin de Kotlin en Android Studio (viene por defecto)
 * 
 * 2. En build.gradle.kts, agregar:
 *    plugins {
 *        id("org.jetbrains.kotlin.android") version "1.9.0"
 *    }
 * 
 * 3. Convertir archivos automáticamente:
 *    - Abrir archivo .java en Android Studio
 *    - Code → Convert Java File to Kotlin File
 *    - Revisar y ajustar el código generado
 * 
 * 4. Ambos lenguajes son 100% interoperables:
 *    - Puedes llamar código Java desde Kotlin
 *    - Puedes llamar código Kotlin desde Java
 *    - Puedes tener ambos en el mismo proyecto
 * 
 * 5. Migración gradual recomendada:
 *    - Nuevos archivos en Kotlin
 *    - Archivos existentes se quedan en Java
 *    - Migrar de a poco según necesidad
 */

