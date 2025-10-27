package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CoachActivity extends AppCompatActivity {

    // Navigation items
    private LinearLayout navInicio;
    private LinearLayout navAnalisis;
    private LinearLayout navSuenos;
    private LinearLayout navCoach;
    private LinearLayout navPresupuesto;

    // Chat elements
    private ScrollView chatScrollView;
    private LinearLayout chatMessagesContainer;
    private EditText etChatInput;
    private Button btnSendMessage;

    // Quick action buttons
    private Button btnReduceExpenses;
    private Button btnAnalyzeBudget;
    private Button btnOptimizeSavings;
    private Button btnInvestmentStrategies;

    // API configuration
    private static final String GROQ_API_KEY = "TU_API_KEY_AQUI";
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String[] FALLBACK_MODELS = {
        "llama-3.1-8b-instant",
        "gemma-7b-it", 
        "mixtral-8x7b-32768"
    };
    private int currentModelIndex = 0;
    
    private OkHttpClient httpClient;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coach);
        
        // Initialize HTTP client and executor
        httpClient = new OkHttpClient();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        
        // Initialize views
        initializeViews();
        
        // Setup navigation
        setupNavigation();
        
        // Setup chat functionality
        setupChat();
        
        // Setup quick actions
        setupQuickActions();
        
        // Test API connection
        testAPIConnection();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        try {
            // Navigation views
            navInicio = findViewById(R.id.nav_inicio);
            navAnalisis = findViewById(R.id.nav_analisis);
            navSuenos = findViewById(R.id.nav_suenos);
            navCoach = findViewById(R.id.nav_coach);
            navPresupuesto = findViewById(R.id.nav_presupuesto);

            // Chat views
            chatScrollView = findViewById(R.id.chat_scroll_view);
            chatMessagesContainer = findViewById(R.id.chat_messages_container);
            etChatInput = findViewById(R.id.et_chat_input);
            btnSendMessage = findViewById(R.id.btn_send_message);

            // Quick action buttons
            btnReduceExpenses = findViewById(R.id.btn_reduce_expenses);
            btnAnalyzeBudget = findViewById(R.id.btn_analyze_budget);
            btnOptimizeSavings = findViewById(R.id.btn_optimize_savings);
            btnInvestmentStrategies = findViewById(R.id.btn_investment_strategies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupNavigation() {
        try {
            if (navInicio != null) {
                navInicio.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navAnalisis != null) {
                navAnalisis.setOnClickListener(v -> {
                    Intent intent = new Intent(this, AnalisisActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navSuenos != null) {
                navSuenos.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MisSueñosActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navCoach != null) {
                navCoach.setOnClickListener(v -> {
                    // Already on coach screen
                    setSelectedNavItem(navCoach);
                });
            }
            
            if (navPresupuesto != null) {
                navPresupuesto.setOnClickListener(v -> {
                    Intent intent = new Intent(this, PresupuestoActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            // Set initial selection
            if (navCoach != null) {
                setSelectedNavItem(navCoach);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupChat() {
        try {
            if (btnSendMessage != null) {
                btnSendMessage.setOnClickListener(v -> {
                    String message = etChatInput.getText().toString().trim();
                    if (!message.isEmpty()) {
                        addUserMessage(message);
                        etChatInput.setText("");
                        sendMessageToAI(message);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupQuickActions() {
        try {
            if (btnReduceExpenses != null) {
                btnReduceExpenses.setOnClickListener(v -> {
                    String prompt = "¿Cómo puedo reducir mis gastos mensuales? Dame consejos específicos y prácticos.";
                    addUserMessage("¿Cómo puedo reducir gastos?");
                    sendMessageToAI(prompt);
                });
            }

            if (btnAnalyzeBudget != null) {
                btnAnalyzeBudget.setOnClickListener(v -> {
                    String prompt = "Analiza mi presupuesto mensual y dame recomendaciones para optimizarlo. Considera que tengo gastos en alimentación, vivienda, transporte, entretenimiento y ahorros.";
                    addUserMessage("Analiza mi presupuesto");
                    sendMessageToAI(prompt);
                });
            }

            if (btnOptimizeSavings != null) {
                btnOptimizeSavings.setOnClickListener(v -> {
                    String prompt = "¿Cómo puedo optimizar mis ahorros? Dame estrategias para aumentar mi capacidad de ahorro mensual.";
                    addUserMessage("Optimiza mis ahorros");
                    sendMessageToAI(prompt);
                });
            }

            if (btnInvestmentStrategies != null) {
                btnInvestmentStrategies.setOnClickListener(v -> {
                    String prompt = "Dame estrategias de inversión para principiantes. ¿Qué opciones tengo para invertir mi dinero de manera segura?";
                    addUserMessage("Estrategias de inversión");
                    sendMessageToAI(prompt);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUserMessage(String message) {
        try {
            mainHandler.post(() -> {
                LinearLayout messageLayout = new LinearLayout(this);
                messageLayout.setOrientation(LinearLayout.HORIZONTAL);
                messageLayout.setPadding(0, 0, 0, 24);

                // User message bubble (right-aligned)
                LinearLayout messageBubble = new LinearLayout(this);
                messageBubble.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.weight = 1;
                messageBubble.setLayoutParams(params);

                // Message text
                TextView messageText = new TextView(this);
                messageText.setText(message);
                messageText.setTextColor(getResources().getColor(android.R.color.white));
                messageText.setTextSize(14);
                messageText.setPadding(16, 12, 16, 12);
                messageText.setBackgroundResource(R.drawable.user_message_bubble);
                messageBubble.addView(messageText);

                // Timestamp
                TextView timestamp = new TextView(this);
                timestamp.setText(getCurrentTime());
                timestamp.setTextColor(getResources().getColor(android.R.color.darker_gray));
                timestamp.setTextSize(10);
                timestamp.setPadding(0, 4, 0, 0);
                messageBubble.addView(timestamp);

                messageLayout.addView(messageBubble);

                // User avatar
                LinearLayout avatar = new LinearLayout(this);
                avatar.setOrientation(LinearLayout.VERTICAL);
                avatar.setGravity(android.view.Gravity.CENTER);
                LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(32, 32);
                avatarParams.leftMargin = 8;
                avatar.setLayoutParams(avatarParams);
                avatar.setBackgroundResource(R.drawable.user_avatar_background);

                TextView avatarText = new TextView(this);
                avatarText.setText("U");
                avatarText.setTextColor(getResources().getColor(android.R.color.white));
                avatarText.setTextSize(12);
                avatarText.setTypeface(null, android.graphics.Typeface.BOLD);
                avatar.addView(avatarText);

                messageLayout.addView(avatar);

                if (chatMessagesContainer != null) {
                    chatMessagesContainer.addView(messageLayout);
                    scrollToBottom();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAIMessage(String message) {
        try {
            mainHandler.post(() -> {
                LinearLayout messageLayout = new LinearLayout(this);
                messageLayout.setOrientation(LinearLayout.HORIZONTAL);
                messageLayout.setPadding(0, 0, 0, 24);

                // AI avatar
                LinearLayout avatar = new LinearLayout(this);
                avatar.setOrientation(LinearLayout.VERTICAL);
                avatar.setGravity(android.view.Gravity.CENTER);
                LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(32, 32);
                avatarParams.rightMargin = 8;
                avatar.setLayoutParams(avatarParams);
                avatar.setBackgroundResource(R.drawable.ai_avatar_background);

                TextView avatarText = new TextView(this);
                avatarText.setText("A");
                avatarText.setTextColor(getResources().getColor(android.R.color.white));
                avatarText.setTextSize(12);
                avatarText.setTypeface(null, android.graphics.Typeface.BOLD);
                avatar.addView(avatarText);

                messageLayout.addView(avatar);

                // AI message bubble (left-aligned)
                LinearLayout messageBubble = new LinearLayout(this);
                messageBubble.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.weight = 1;
                messageBubble.setLayoutParams(params);

                // Message text
                TextView messageText = new TextView(this);
                messageText.setText(message);
                messageText.setTextColor(getResources().getColor(android.R.color.black));
                messageText.setTextSize(14);
                messageText.setPadding(16, 12, 16, 12);
                messageText.setBackgroundResource(R.drawable.ai_message_bubble);
                messageBubble.addView(messageText);

                // Timestamp
                TextView timestamp = new TextView(this);
                timestamp.setText(getCurrentTime());
                timestamp.setTextColor(getResources().getColor(android.R.color.darker_gray));
                timestamp.setTextSize(10);
                timestamp.setPadding(0, 4, 0, 0);
                messageBubble.addView(timestamp);

                messageLayout.addView(messageBubble);

                if (chatMessagesContainer != null) {
                    chatMessagesContainer.addView(messageLayout);
                    scrollToBottom();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToAI(String message) {
        try {
            executorService.execute(() -> {
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("model", FALLBACK_MODELS[currentModelIndex]); // Usar modelo actual
                    requestBody.put("messages", new org.json.JSONArray()
                        .put(new JSONObject()
                            .put("role", "system")
                            .put("content", "Eres un coach financiero experto que ayuda a usuarios con consejos prácticos sobre finanzas personales, presupuestos, ahorros e inversiones. Responde en español de manera clara y concisa."))
                        .put(new JSONObject()
                            .put("role", "user")
                            .put("content", message)));
                    requestBody.put("max_tokens", 300); // Reducido para mejor estabilidad
                    requestBody.put("temperature", 0.7);
                    requestBody.put("stream", false); // Asegurar que no sea streaming

                    RequestBody body = RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                        .url(GROQ_API_URL)
                        .addHeader("Authorization", "Bearer " + GROQ_API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("User-Agent", "Android-App/1.0")
                        .post(body)
                        .build();

                    httpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            mainHandler.post(() -> {
                                addAIMessage("Lo siento, no pude procesar tu consulta en este momento. Por favor, inténtalo de nuevo.");
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseBody = response.body().string();
                            
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(responseBody);
                                    JSONObject choice = jsonResponse
                                        .getJSONArray("choices")
                                        .getJSONObject(0);
                                    String aiMessage = choice
                                        .getJSONObject("message")
                                        .getString("content");

                                    mainHandler.post(() -> {
                                        addAIMessage(aiMessage);
                                    });
                                } catch (JSONException e) {
                                    mainHandler.post(() -> {
                                        addAIMessage("Error al procesar la respuesta del servidor.");
                                    });
                                }
                            } else {
                                // Intentar con el siguiente modelo si hay error
                                if (currentModelIndex < FALLBACK_MODELS.length - 1) {
                                    currentModelIndex++;
                                    mainHandler.post(() -> {
                                        addAIMessage("Reintentando con otro modelo...");
                                    });
                                    // Reintentar con el siguiente modelo
                                    sendMessageToAI(message);
                                    return;
                                }
                                
                                String errorMessage = "Error del servidor: " + response.code();
                                try {
                                    JSONObject errorResponse = new JSONObject(responseBody);
                                    if (errorResponse.has("error")) {
                                        JSONObject error = errorResponse.getJSONObject("error");
                                        if (error.has("message")) {
                                            errorMessage = error.getString("message");
                                        }
                                    }
                                } catch (JSONException ignored) {
                                    // Usar mensaje por defecto si no se puede parsear
                                }
                                
                                final String finalErrorMessage = errorMessage;
                                mainHandler.post(() -> {
                                    addAIMessage("Error: " + finalErrorMessage + ". Por favor, inténtalo más tarde.");
                                });
                            }
                        }
                    });
                } catch (Exception e) {
                    mainHandler.post(() -> {
                        addAIMessage("Error al enviar la consulta. Por favor, inténtalo de nuevo.");
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        try {
            if (chatScrollView != null) {
                chatScrollView.post(() -> {
                    chatScrollView.fullScroll(View.FOCUS_DOWN);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
            return sdf.format(new java.util.Date());
        } catch (Exception e) {
            return "Ahora";
        }
    }
    
    private void setSelectedNavItem(LinearLayout selectedItem) {
        try {
            // Reset all navigation items
            if (navInicio != null) navInicio.setBackground(null);
            if (navAnalisis != null) navAnalisis.setBackground(null);
            if (navSuenos != null) navSuenos.setBackground(null);
            if (navCoach != null) navCoach.setBackground(null);
            if (navPresupuesto != null) navPresupuesto.setBackground(null);
            
            // Set selected item background
            if (selectedItem != null) {
                selectedItem.setBackgroundResource(R.drawable.nav_item_selected_background);
                updateNavTextColors(selectedItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateNavTextColors(LinearLayout selectedItem) {
        try {
            // Reset all text colors to default
            if (navInicio != null && navInicio.getChildCount() > 1) {
                TextView textInicio = (TextView) navInicio.getChildAt(1);
                textInicio.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navAnalisis != null && navAnalisis.getChildCount() > 1) {
                TextView textAnalisis = (TextView) navAnalisis.getChildAt(1);
                textAnalisis.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navSuenos != null && navSuenos.getChildCount() > 1) {
                TextView textSuenos = (TextView) navSuenos.getChildAt(1);
                textSuenos.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navCoach != null && navCoach.getChildCount() > 1) {
                TextView textCoach = (TextView) navCoach.getChildAt(1);
                textCoach.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navPresupuesto != null && navPresupuesto.getChildCount() > 1) {
                TextView textPresupuesto = (TextView) navPresupuesto.getChildAt(1);
                textPresupuesto.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            
            // Set selected text color
            if (selectedItem != null && selectedItem.getChildCount() > 1) {
                TextView selectedText = (TextView) selectedItem.getChildAt(1);
                selectedText.setTextColor(getResources().getColor(R.color.primary_blue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testAPIConnection() {
        try {
            executorService.execute(() -> {
                try {
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("model", FALLBACK_MODELS[currentModelIndex]);
                    requestBody.put("messages", new org.json.JSONArray()
                        .put(new JSONObject()
                            .put("role", "user")
                            .put("content", "Hola")));
                    requestBody.put("max_tokens", 50);
                    requestBody.put("temperature", 0.7);

                    RequestBody body = RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                    );

                    Request request = new Request.Builder()
                        .url(GROQ_API_URL)
                        .addHeader("Authorization", "Bearer " + GROQ_API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();

                    Response response = httpClient.newCall(request).execute();
                    String responseBody = response.body().string();
                    
                    mainHandler.post(() -> {
                        if (response.isSuccessful()) {
                            addAIMessage("¡Hola! Soy tu coach financiero personal. ¿En qué puedo ayudarte hoy?");
                        } else {
                            addAIMessage("⚠️ Hay un problema con la conexión al servidor. Algunas funciones pueden no estar disponibles.");
                        }
                    });
                } catch (Exception e) {
                    mainHandler.post(() -> {
                        addAIMessage("⚠️ Error de conexión. Verifica tu conexión a internet y vuelve a intentar.");
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
