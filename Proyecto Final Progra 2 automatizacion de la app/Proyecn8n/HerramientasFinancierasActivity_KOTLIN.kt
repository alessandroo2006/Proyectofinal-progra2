// ═══════════════════════════════════════════════════════════════════════
// 📘 IMPLEMENTACIÓN COMPLETA EN KOTLIN
// ═══════════════════════════════════════════════════════════════════════
//
// NOTA: Este archivo es SOLO de REFERENCIA.
// Tu app usa Java, por lo que la implementación funcional está en:
// FinancialToolsActivity.java
//
// Este código muestra cómo sería la misma implementación en Kotlin.
//
// ═══════════════════════════════════════════════════════════════════════

package com.example.prueba

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
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

// ═══════════════════════════════════════════════════════════════════════
// 1️⃣ DEPENDENCIAS - build.gradle.kts
// ═══════════════════════════════════════════════════════════════════════

/*
dependencies {
    // Retrofit y Gson (Ya están instaladas en tu proyecto)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
}
*/

// ═══════════════════════════════════════════════════════════════════════
// 2️⃣ MODELO DE DATOS - Data Class
// ═══════════════════════════════════════════════════════════════════════

/**
 * Data class que representa el evento a enviar al webhook de n8n
 * 
 * JSON generado:
 * {
 *   "evento": "clasificador_gastos",
 *   "timestamp": 1735123456789,
 *   "usuario_id": "123"
 * }
 */
data class EventoN8n(
    @SerializedName("evento")
    val evento: String,
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @SerializedName("usuario_id")
    val usuarioId: String? = null
)

// ═══════════════════════════════════════════════════════════════════════
// 3️⃣ INTERFAZ DE API - ApiService con Retrofit
// ═══════════════════════════════════════════════════════════════════════

/**
 * Interfaz que define los endpoints del webhook de n8n
 */
interface N8nApiService {
    
    /**
     * Envía un evento al webhook de n8n
     * 
     * Endpoint completo:
     * POST https://foxyti.app.n8n.cloud/webhook-test/a287f99f-f2df-45c8-b82a-cc1a1afd0f81
     */
    @POST("webhook-test/a287f99f-f2df-45c8-b82a-cc1a1afd0f81")
    fun sendEvent(@Body evento: EventoN8n): Call<Void>
}

// ═══════════════════════════════════════════════════════════════════════
// 4️⃣ CLIENTE RETROFIT - Singleton Object
// ═══════════════════════════════════════════════════════════════════════

/**
 * Object singleton que proporciona la instancia única de Retrofit
 * 
 * En Kotlin, 'object' es automáticamente un singleton thread-safe
 */
object RetrofitClient {
    
    // URL base del webhook
    private const val BASE_URL = "https://foxyti.app.n8n.cloud/"
    
    /**
     * Instancia lazy de Retrofit
     * Se crea solo cuando se accede por primera vez
     */
    private val retrofit: Retrofit by lazy {
        // Configurar interceptor de logs para debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Configurar cliente OkHttp con timeouts
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        // Crear y configurar Retrofit
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

// ═══════════════════════════════════════════════════════════════════════
// 5️⃣ ACTIVITY - HerramientasFinancierasActivity
// ═══════════════════════════════════════════════════════════════════════

/**
 * Activity principal de Herramientas Financieras
 * 
 * Funcionalidad:
 * - Notifica a n8n cuando se abre la pantalla
 * - Notifica a n8n cuando se presiona cada herramienta
 */
class HerramientasFinancierasActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "FinancialTools"
    }
    
    // Referencias a las vistas (CardViews de cada herramienta)
    private lateinit var cardNotificador: CardView
    private lateinit var cardClasificador: CardView
    private lateinit var cardAlertas: CardView
    private lateinit var cardAsistente: CardView
    private lateinit var cardLogros: CardView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_tools)
        
        // Inicializar vistas
        initializeViews()
        
        // Configurar listeners de los botones
        setupClickListeners()
        
        // ⭐ NOTIFICAR QUE SE ABRIÓ LA PANTALLA
        notificarSeleccionDeHerramienta("usuario_ingreso_a_herramientas")
    }
    
    /**
     * Inicializar las referencias a las vistas
     */
    private fun initializeViews() {
        cardNotificador = findViewById(R.id.btn_menu_subscriptions)
        cardClasificador = findViewById(R.id.btn_menu_expense_classifier)
        cardAlertas = findViewById(R.id.btn_menu_deal_hunter)
        cardAsistente = findViewById(R.id.btn_menu_voice_assistant)
        cardLogros = findViewById(R.id.btn_menu_gamification)
    }
    
    /**
     * Configurar los click listeners de cada herramienta
     */
    private fun setupClickListeners() {
        
        // ⭐ 1. Notificador de Suscripciones
        cardNotificador.setOnClickListener {
            // Notificar a n8n
            notificarSeleccionDeHerramienta("notificador_suscripciones")
            
            // Navegar a la actividad
            // startActivity(Intent(this, SubscriptionTrackerActivity::class.java))
        }
        
        // ⭐ 2. Clasificador de Gastos
        cardClasificador.setOnClickListener {
            // Notificar a n8n
            notificarSeleccionDeHerramienta("clasificador_gastos")
            
            // Navegar a la actividad
            // startActivity(Intent(this, ExpenseClassifierActivity::class.java))
        }
        
        // ⭐ 3. Alertas de Ofertas
        cardAlertas.setOnClickListener {
            // Notificar a n8n
            notificarSeleccionDeHerramienta("alertas_ofertas")
            
            // Navegar a la actividad
            // startActivity(Intent(this, DealAlertsActivity::class.java))
        }
        
        // ⭐ 4. Asistente por Voz
        cardAsistente.setOnClickListener {
            // Notificar a n8n
            notificarSeleccionDeHerramienta("asistente_voz")
            
            // Mostrar modal o navegar
            // showVoiceAssistantModal()
        }
        
        // ⭐ 5. Logros y Retos
        cardLogros.setOnClickListener {
            // Notificar a n8n
            notificarSeleccionDeHerramienta("logros_y_retos")
            
            // Navegar a la actividad
            // startActivity(Intent(this, AchievementsActivity::class.java))
        }
    }
    
    /**
     * ⭐ FUNCIÓN REUTILIZABLE: Notifica a n8n cuando se selecciona una herramienta
     * 
     * @param identificador Identificador único de la herramienta
     * 
     * Identificadores válidos:
     * - "usuario_ingreso_a_herramientas" → Al abrir la pantalla
     * - "notificador_suscripciones" → Notificador de Suscripciones
     * - "clasificador_gastos" → Clasificador de Gastos
     * - "alertas_ofertas" → Alertas de Ofertas
     * - "asistente_voz" → Asistente por Voz
     * - "logros_y_retos" → Logros y Retos
     */
    private fun notificarSeleccionDeHerramienta(identificador: String) {
        try {
            // Obtener ID del usuario (opcional)
            val usuarioId = try {
                // SessionManager(this).userId.toString()
                null // Por ahora null
            } catch (e: Exception) {
                Log.w(TAG, "No se pudo obtener userId")
                null
            }
            
            // Crear objeto EventoN8n
            val evento = EventoN8n(
                evento = identificador,
                usuarioId = usuarioId
            )
            
            // Logs de debugging
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d(TAG, "📤 Enviando evento al webhook de n8n")
            Log.d(TAG, "🎯 Herramienta: $identificador")
            Log.d(TAG, "👤 Usuario ID: ${usuarioId ?: "N/A"}")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            
            // Obtener servicio API
            val apiService = RetrofitClient.apiService
            
            // Crear la llamada
            val call = apiService.sendEvent(evento)
            
            // Ejecutar llamada asíncrona
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    // ✅ RESPUESTA EXITOSA
                    if (response.isSuccessful) {
                        Log.d(TAG, "")
                        Log.d(TAG, "✅ ¡ÉXITO! Evento enviado correctamente")
                        Log.d(TAG, "📊 Código HTTP: ${response.code()}")
                        Log.d(TAG, "🎯 Evento: $identificador")
                        Log.d(TAG, "⏱️ Timestamp: ${evento.timestamp}")
                        Log.d(TAG, "")
                    } else {
                        // ⚠️ RESPUESTA CON ERROR HTTP
                        Log.w(TAG, "")
                        Log.w(TAG, "⚠️ Webhook respondió con error")
                        Log.w(TAG, "📊 Código HTTP: ${response.code()}")
                        Log.w(TAG, "📝 Mensaje: ${response.message()}")
                        Log.w(TAG, "🎯 Evento: $identificador")
                        Log.w(TAG, "")
                    }
                }
                
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // ❌ ERROR DE RED
                    Log.e(TAG, "")
                    Log.e(TAG, "❌ ERROR DE RED al enviar evento")
                    Log.e(TAG, "🎯 Evento: $identificador")
                    Log.e(TAG, "💥 Error: ${t.javaClass.simpleName}")
                    Log.e(TAG, "📝 Mensaje: ${t.message}")
                    Log.e(TAG, "")
                    Log.e(TAG, "💡 Posibles causas:")
                    Log.e(TAG, "   • Sin conexión a Internet")
                    Log.e(TAG, "   • Timeout (más de 30 segundos)")
                    Log.e(TAG, "   • Servidor n8n no disponible")
                    Log.e(TAG, "")
                    
                    // La app continúa funcionando normalmente
                }
            })
            
        } catch (e: Exception) {
            // ❌ ERROR INESPERADO
            Log.e(TAG, "")
            Log.e(TAG, "❌ ERROR INESPERADO")
            Log.e(TAG, "🎯 Evento: $identificador")
            Log.e(TAG, "💥 Excepción: ${e.javaClass.simpleName}")
            Log.e(TAG, "📝 Mensaje: ${e.message}")
            Log.e(TAG, "")
            e.printStackTrace()
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// 📊 EJEMPLO DE JSON ENVIADO
// ═══════════════════════════════════════════════════════════════════════

/*
Al presionar "Clasificador de Gastos", se envía:

POST https://foxyti.app.n8n.cloud/webhook-test/a287f99f-f2df-45c8-b82a-cc1a1afd0f81

Body:
{
  "evento": "clasificador_gastos",
  "timestamp": 1735123456789,
  "usuario_id": "123"
}
*/

// ═══════════════════════════════════════════════════════════════════════
// 📋 TODOS LOS IDENTIFICADORES POSIBLES
// ═══════════════════════════════════════════════════════════════════════

/*
1. "usuario_ingreso_a_herramientas"  → Al abrir la pantalla
2. "notificador_suscripciones"       → Notificador de Suscripciones
3. "clasificador_gastos"             → Clasificador de Gastos
4. "alertas_ofertas"                 → Alertas de Ofertas / Cazador
5. "asistente_voz"                   → Asistente por Voz
6. "logros_y_retos"                  → Gamificación y Retos
*/

// ═══════════════════════════════════════════════════════════════════════
// 🔍 LOGS EN LOGCAT (Filtro: "FinancialTools")
// ═══════════════════════════════════════════════════════════════════════

/*
EJEMPLO DE LOG EXITOSO:

D/FinancialTools: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
D/FinancialTools: 📤 Enviando evento al webhook de n8n
D/FinancialTools: 🎯 Herramienta: clasificador_gastos
D/FinancialTools: 👤 Usuario ID: 123
D/FinancialTools: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
D/FinancialTools: 
D/FinancialTools: ✅ ¡ÉXITO! Evento enviado correctamente
D/FinancialTools: 📊 Código HTTP: 200
D/FinancialTools: 🎯 Evento: clasificador_gastos
D/FinancialTools: ⏱️ Timestamp: 1735123456789
D/FinancialTools: 

EJEMPLO DE LOG CON ERROR:

D/FinancialTools: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
D/FinancialTools: 📤 Enviando evento al webhook de n8n
D/FinancialTools: 🎯 Herramienta: alertas_ofertas
D/FinancialTools: 👤 Usuario ID: N/A
D/FinancialTools: ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
E/FinancialTools: 
E/FinancialTools: ❌ ERROR DE RED al enviar evento
E/FinancialTools: 🎯 Evento: alertas_ofertas
E/FinancialTools: 💥 Error: UnknownHostException
E/FinancialTools: 📝 Mensaje: Unable to resolve host...
E/FinancialTools: 
E/FinancialTools: 💡 Posibles causas:
E/FinancialTools:    • Sin conexión a Internet
E/FinancialTools:    • Timeout (más de 30 segundos)
E/FinancialTools:    • Servidor n8n no disponible
E/FinancialTools: 
*/

// ═══════════════════════════════════════════════════════════════════════
// 🎯 VENTAJAS DE KOTLIN VS JAVA
// ═══════════════════════════════════════════════════════════════════════

/*
1. DATA CLASSES:
   Java:   60+ líneas (constructor, getters, setters, equals, hashCode, toString)
   Kotlin: 5 líneas ✅
   
   data class EventoN8n(val evento: String, val timestamp: Long)

2. NULL SAFETY:
   Java:   if (obj != null) { obj.doSomething(); }
   Kotlin: obj?.doSomething() ✅

3. STRING TEMPLATES:
   Java:   "Código: " + response.code()
   Kotlin: "Código: ${response.code()}" ✅

4. LAMBDAS MÁS LIMPIAS:
   Java:   view.setOnClickListener(v -> { ... })
   Kotlin: view.setOnClickListener { ... } ✅

5. PROPERTIES EN LUGAR DE GETTERS:
   Java:   response.code()
   Kotlin: response.code ✅ (sin paréntesis)

6. ELVIS OPERATOR:
   Java:   String id = userId != null ? userId : "N/A";
   Kotlin: val id = userId ?: "N/A" ✅

7. WHEN EN LUGAR DE SWITCH:
   Java:   switch(x) { case 1: ...; break; }
   Kotlin: when(x) { 1 -> ... } ✅

8. EXTENSION FUNCTIONS:
   Java:   Utils.formatDate(date)
   Kotlin: date.format() ✅

9. COROUTINES (async/await nativo):
   Java:   .enqueue(callback)
   Kotlin: suspend fun + coroutineScope ✅

10. INTEROPERABILIDAD 100%:
    - Puedes mezclar Java y Kotlin en el mismo proyecto
    - Llama código Java desde Kotlin y viceversa
    - Migración gradual sin reescribir todo
*/

// ═══════════════════════════════════════════════════════════════════════
// 📚 PARA APRENDER MÁS SOBRE KOTLIN
// ═══════════════════════════════════════════════════════════════════════

/*
Recursos oficiales:
- https://kotlinlang.org/docs/home.html
- https://developer.android.com/kotlin

Codelab Android con Kotlin:
- https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app

Migración Java → Kotlin:
- Android Studio: Code → Convert Java File to Kotlin File
- Revisa y ajusta el código generado
- Ambos lenguajes coexisten perfectamente
*/

