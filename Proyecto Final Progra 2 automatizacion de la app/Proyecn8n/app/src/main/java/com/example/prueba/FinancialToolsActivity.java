package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.prueba.api.N8nApiService;
import com.example.prueba.api.N8nRetrofitClient;
import com.example.prueba.models.EventoN8n;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancialToolsActivity extends AppCompatActivity {
    
    private static final String TAG = "FinancialTools";

    // Menu option buttons
    private CardView btnMenuSubscriptions;
    private CardView btnMenuExpenseClassifier;
    private CardView btnMenuDealHunter;
    private CardView btnMenuVoiceAssistant;
    private CardView btnMenuGamification;
    
    // ViewModel for expenses
    private ExpenseViewModel expenseViewModel;
    private N8nWebhookClient webhookClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_financial_tools);
            
            // Initialize ViewModel and Webhook Client
            try {
                expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing ExpenseViewModel", e);
            }
            
            try {
                webhookClient = N8nWebhookClient.getInstance();
            } catch (Exception e) {
                Log.e(TAG, "Error initializing WebhookClient", e);
            }
            
            // Initialize views
            initializeViews();
            
            // Setup click listeners
            setupClickListeners();
            
            // ⭐ ENVIAR NOTIFICACIÓN AL WEBHOOK DE N8N
            enviarEventoAWebhook();
        } catch (Exception e) {
            Log.e(TAG, "Critical error in onCreate", e);
            Toast.makeText(this, "Error al inicializar la aplicación", Toast.LENGTH_SHORT).show();
        }
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        try {
            btnMenuSubscriptions = findViewById(R.id.btn_menu_subscriptions);
            btnMenuExpenseClassifier = findViewById(R.id.btn_menu_expense_classifier);
            btnMenuDealHunter = findViewById(R.id.btn_menu_deal_hunter);
            btnMenuVoiceAssistant = findViewById(R.id.btn_menu_voice_assistant);
            btnMenuGamification = findViewById(R.id.btn_menu_gamification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupClickListeners() {
        try {
            // ⭐ Notificador de Suscripciones
            if (btnMenuSubscriptions != null) {
                btnMenuSubscriptions.setOnClickListener(v -> {
                    // Notificar a n8n que se seleccionó esta herramienta
                    notificarSeleccionDeHerramienta("notificador_suscripciones");
                    
                    // Navegar a la actividad
                    Intent intent = new Intent(this, SubscriptionTrackerActivity.class);
                    startActivity(intent);
                });
            }
            
            // ⭐ Clasificador de Gastos
            if (btnMenuExpenseClassifier != null) {
                btnMenuExpenseClassifier.setOnClickListener(v -> {
                    // Notificar a n8n que se seleccionó esta herramienta
                    notificarSeleccionDeHerramienta("clasificador_gastos");
                    
                    // Navegar a la actividad
                    Intent intent = new Intent(this, ExpenseClassifierSetupActivity.class);
                    startActivity(intent);
                });
            }
            
            // ⭐ Cazador de Ofertas / Alertas de Ofertas
            if (btnMenuDealHunter != null) {
                btnMenuDealHunter.setOnClickListener(v -> {
                    // Notificar a n8n que se seleccionó esta herramienta
                    notificarSeleccionDeHerramienta("alertas_ofertas");
                    
                    // Navegar a la actividad
                    Intent intent = new Intent(this, DealAlertsActivity.class);
                    startActivity(intent);
                });
            }
            
            // ⭐ Grabador de Gasto (antes: Asistente por Voz)
            if (btnMenuVoiceAssistant != null) {
                btnMenuVoiceAssistant.setOnClickListener(v -> {
                    // Notificar a n8n que se seleccionó esta herramienta
                    notificarSeleccionDeHerramienta("grabador_gasto");
                    
                    // Mostrar modal de grabador de gasto por voz
                    showVoiceExpenseModal();
                });
            }
            
            // ⭐ Gamificación y Retos / Logros y Retos
            if (btnMenuGamification != null) {
                btnMenuGamification.setOnClickListener(v -> {
                    // Notificar a n8n que se seleccionó esta herramienta
                    notificarSeleccionDeHerramienta("logros_y_retos");
                    
                    // Navegar a la actividad
                    Intent intent = new Intent(this, AchievementsActivity.class);
                    startActivity(intent);
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showVoiceExpenseModal() {
        VoiceInputBottomSheet bottomSheet = VoiceInputBottomSheet.newInstance();
        bottomSheet.setCallback(new VoiceInputBottomSheet.VoiceInputCallback() {
            @Override
            public void onVoiceExpenseConfirmed(String amount, String merchant, String category) {
                // Guardar el gasto en la base de datos
                saveExpenseToDatabase(amount, merchant, category);
            }
            
            @Override
            public void onVoiceInputCancelled() {
                Toast.makeText(FinancialToolsActivity.this, 
                    "❌ Grabación cancelada", Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheet.show(getSupportFragmentManager(), "VoiceExpenseRecorder");
    }
    
    /**
     * Guarda el gasto capturado por voz en la base de datos local
     * y lo envía a n8n para sincronización
     */
    private void saveExpenseToDatabase(String amountStr, String merchant, String category) {
        try {
            // Validar inputs
            if (amountStr == null || amountStr.trim().isEmpty()) {
                Toast.makeText(this, "⚠️ Monto inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (merchant == null || merchant.trim().isEmpty()) {
                merchant = "Comercio desconocido";
            }
            
            if (category == null || category.trim().isEmpty()) {
                category = "Otros";
            }
            
            // Limpiar el monto (quitar "Q" y espacios)
            String cleanAmount = amountStr.replace("Q", "").replace(" ", "").trim();
            double amount = Double.parseDouble(cleanAmount);
            
            // Validar que el monto sea válido
            if (amount <= 0) {
                Toast.makeText(this, "⚠️ El monto debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Obtener ID del usuario actual
            SessionManager sessionManager = new SessionManager(this);
            int userId = sessionManager.getUserId();
            
            // Verificar que el usuario esté logueado
            if (userId <= 0) {
                Toast.makeText(this, "⚠️ Usuario no identificado", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Invalid userId: " + userId);
                return;
            }
            
            // Crear el objeto Expense
            Expense expense = new Expense(
                amount,
                merchant,
                category,
                System.currentTimeMillis(),
                "voice",  // Método de entrada
                "Capturado por voz",  // Texto original
                userId
            );
            
            // Guardar en la base de datos
            if (expenseViewModel != null) {
                expenseViewModel.insert(expense);
            } else {
                Log.e(TAG, "ExpenseViewModel is null");
                Toast.makeText(this, "⚠️ Error: Sistema no inicializado", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Enviar a n8n
            sendExpenseToN8n(expense);
            
            // Mostrar confirmación al usuario
            Toast.makeText(this, 
                "✅ Gasto registrado: " + amountStr + " en " + merchant, 
                Toast.LENGTH_LONG).show();
            
            Log.d(TAG, "Gasto guardado: " + amountStr + " en " + merchant + " (" + category + ")");
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, 
                "⚠️ Error al procesar el monto: " + amountStr, 
                Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error parsing amount: " + amountStr, e);
        } catch (Exception e) {
            Toast.makeText(this, 
                "⚠️ Error al guardar el gasto", 
                Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error saving expense", e);
        }
    }
    
    /**
     * Envía los datos del gasto a n8n para sincronización
     */
    private void sendExpenseToN8n(Expense expense) {
        if (expense == null) {
            Log.e(TAG, "Cannot send null expense to n8n");
            return;
        }
        
        if (webhookClient == null) {
            Log.e(TAG, "WebhookClient is null, cannot send to n8n");
            return;
        }
        
        try {
            SessionManager sessionManager = new SessionManager(this);
            String userId = String.valueOf(sessionManager.getUserId());
            String userName = sessionManager.getUsername();
            String userEmail = sessionManager.getEmail();
            
            // Validar datos
            if (userName == null || userName.isEmpty()) {
                userName = "Usuario";
            }
            if (userEmail == null || userEmail.isEmpty()) {
                userEmail = "no-email@example.com";
            }
            
            // Crear payload con los datos del gasto
            Map<String, Object> expenseData = new HashMap<>();
            expenseData.put("herramienta", "grabador_gasto");
            expenseData.put("cliente", userName);
            expenseData.put("email", userEmail);
            expenseData.put("monto", expense.getAmount());
            expenseData.put("negocio", expense.getMerchant());
            expenseData.put("categoria", expense.getCategory());
            expenseData.put("fecha", expense.getFormattedDate());
            expenseData.put("metodo_entrada", expense.getInputMethod());
            
            webhookClient.sendEventToWebhook(
                "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6",
                "voice_expense_saved",
                expenseData,
                userId,
                new N8nWebhookClient.WebhookCallback() {
                    @Override
                    public void onSuccess(N8nWebhookService.N8nResponse response) {
                        Log.d(TAG, "✅ Gasto enviado a n8n exitosamente");
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ Error al enviar gasto a n8n: " + error);
                        // No mostrar error al usuario, es background sync
                    }
                }
            );
        } catch (Exception e) {
            Log.e(TAG, "Exception sending expense to n8n", e);
            // No mostrar error al usuario, es background sync
        }
    }
    
    /**
     * ⭐ MÉTODO PRINCIPAL: Envía evento al webhook de n8n usando Retrofit
     * Se ejecuta automáticamente cuando el usuario abre esta pantalla (en onCreate)
     */
    private void enviarEventoAWebhook() {
        notificarSeleccionDeHerramienta("usuario_ingreso_a_herramientas");
    }
    
    /**
     * ⭐ FUNCIÓN REUTILIZABLE: Notifica a n8n cuando se selecciona una herramienta
     * 
     * @param identificador Identificador único de la herramienta seleccionada
     * 
     * Identificadores válidos:
     * - "usuario_ingreso_a_herramientas" (cuando abre la pantalla)
     * - "notificador_suscripciones" (cuando presiona Notificador de Suscripciones)
     * - "clasificador_gastos" (cuando presiona Clasificador de Gastos)
     * - "alertas_ofertas" (cuando presiona Cazador de Ofertas)
     * - "grabador_gasto" (cuando presiona Grabador de Gasto)
     * - "logros_y_retos" (cuando presiona Gamificación y Retos)
     */
    private void notificarSeleccionDeHerramienta(String identificador) {
        try {
            // Obtener ID del usuario (opcional)
            String usuarioId = null;
            try {
                usuarioId = String.valueOf(new SessionManager(this).getUserId());
            } catch (Exception e) {
                Log.w(TAG, "No se pudo obtener userId, continuando sin él");
            }
            
            // Crear el objeto EventoN8n con el identificador específico
            EventoN8n evento = new EventoN8n(identificador, usuarioId);
            
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            Log.d(TAG, "📤 Enviando evento al webhook de n8n");
            Log.d(TAG, "🎯 Herramienta: " + identificador);
            Log.d(TAG, "👤 Usuario ID: " + (usuarioId != null ? usuarioId : "N/A"));
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            // Obtener la instancia del servicio API
            N8nApiService apiService = N8nRetrofitClient.getApiService();
            
            // Crear la llamada (Call) al endpoint
            Call<Void> call = apiService.enviarEvento(evento);
            
            // Ejecutar la llamada de forma asíncrona (en segundo plano)
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // ✅ RESPUESTA EXITOSA
                    if (response.isSuccessful()) {
                        Log.d(TAG, "");
                        Log.d(TAG, "✅ ¡ÉXITO! Evento enviado correctamente");
                        Log.d(TAG, "📊 Código HTTP: " + response.code());
                        Log.d(TAG, "🎯 Evento: " + identificador);
                        Log.d(TAG, "⏱️ Timestamp: " + evento.getTimestamp());
                        Log.d(TAG, "");
                        
                        // Opcional: Mostrar Toast de confirmación
                        // runOnUiThread(() -> 
                        //     Toast.makeText(FinancialToolsActivity.this, 
                        //         "✓ " + identificador + " registrado", 
                        //         Toast.LENGTH_SHORT).show()
                        // );
                    } else {
                        // ⚠️ RESPUESTA CON ERROR HTTP (404, 500, etc.)
                        Log.w(TAG, "");
                        Log.w(TAG, "⚠️ Webhook respondió con error");
                        Log.w(TAG, "📊 Código HTTP: " + response.code());
                        Log.w(TAG, "📝 Mensaje: " + response.message());
                        Log.w(TAG, "🎯 Evento que se intentó enviar: " + identificador);
                        Log.w(TAG, "");
                    }
                }
                
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // ❌ ERROR DE RED (sin conexión, timeout, etc.)
                    Log.e(TAG, "");
                    Log.e(TAG, "❌ ERROR DE RED al enviar evento");
                    Log.e(TAG, "🎯 Evento: " + identificador);
                    Log.e(TAG, "💥 Error: " + t.getClass().getSimpleName());
                    Log.e(TAG, "📝 Mensaje: " + t.getMessage());
                    Log.e(TAG, "");
                    Log.e(TAG, "💡 Posibles causas:");
                    Log.e(TAG, "   • Sin conexión a Internet");
                    Log.e(TAG, "   • Timeout (más de 30 segundos)");
                    Log.e(TAG, "   • Servidor n8n no disponible");
                    Log.e(TAG, "");
                    
                    // La app continúa funcionando normalmente aunque falle el webhook
                    // No mostramos error al usuario para no interrumpir su experiencia
                }
            });
            
        } catch (Exception e) {
            // ❌ ERROR INESPERADO
            Log.e(TAG, "");
            Log.e(TAG, "❌ ERROR INESPERADO al intentar enviar evento");
            Log.e(TAG, "🎯 Evento: " + identificador);
            Log.e(TAG, "💥 Excepción: " + e.getClass().getSimpleName());
            Log.e(TAG, "📝 Mensaje: " + e.getMessage());
            Log.e(TAG, "");
            e.printStackTrace();
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
