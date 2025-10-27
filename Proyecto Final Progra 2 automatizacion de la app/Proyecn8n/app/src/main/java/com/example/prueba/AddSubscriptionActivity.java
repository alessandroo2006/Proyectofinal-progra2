package com.example.prueba;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddSubscriptionActivity extends AppCompatActivity {
    
    private static final String TAG = "AddSubscriptionActivity";
    private static final String N8N_SUBSCRIPTION_WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6";
    
    private SubscriptionViewModel viewModel;
    private TextInputEditText etSubName;
    private TextInputEditText etSubAmount;
    private Spinner spinnerSubFrequency;
    private TextView etSubRenewalDate;
    private Button btnSaveSubscription;
    private N8nWebhookClient webhookClient;
    
    private Calendar selectedDate;
    private String selectedFrequency;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_subscription);
        
        // Initialize webhook client
        webhookClient = N8nWebhookClient.getInstance();
        
        // Initialize views
        initializeViews();
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        
        // Setup frequency spinner
        setupFrequencySpinner();
        
        // Setup click listeners
        setupClickListeners();
        
        // Setup observers
        setupObservers();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        etSubName = findViewById(R.id.et_sub_name);
        etSubAmount = findViewById(R.id.et_sub_amount);
        spinnerSubFrequency = findViewById(R.id.spinner_sub_frequency);
        etSubRenewalDate = findViewById(R.id.et_sub_renewal_date);
        btnSaveSubscription = findViewById(R.id.btn_save_subscription);
    }
    
    private void setupFrequencySpinner() {
        String[] frequencies = {"MENSUAL", "TRIMESTRAL", "ANUAL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frequencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubFrequency.setAdapter(adapter);
        
        spinnerSubFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFrequency = frequencies[position];
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFrequency = "MENSUAL";
            }
        });
    }
    
    private void setupClickListeners() {
        etSubRenewalDate.setOnClickListener(v -> showDatePicker());
        
        btnSaveSubscription.setOnClickListener(v -> saveSubscription());
    }
    
    private void setupObservers() {
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    
                    String formattedDate = String.format("%02d/%02d/%04d", 
                            selectedDay, selectedMonth + 1, selectedYear);
                    etSubRenewalDate.setText(formattedDate);
                }, year, month, day);
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    
    private void saveSubscription() {
        String name = etSubName.getText().toString().trim();
        String amountText = etSubAmount.getText().toString().trim();
        String renewalDateText = etSubRenewalDate.getText().toString().trim();
        
        // Validate inputs
        if (name.isEmpty()) {
            etSubName.setError("El nombre es requerido");
            return;
        }
        
        if (amountText.isEmpty()) {
            etSubAmount.setError("El monto es requerido");
            return;
        }
        
        if (renewalDateText.isEmpty()) {
            Toast.makeText(this, "Selecciona una fecha de renovación", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountText);
            long renewalDate = selectedDate.getTimeInMillis();
            
            // Create subscription
            Subscription subscription = new Subscription(
                    name,
                    amount,
                    "Q", // Default currency
                    selectedFrequency,
                    renewalDate,
                    7 // Default notification days before
            );
            
            // Save subscription locally
            viewModel.insert(subscription);
            
            // Send subscription data to n8n
            sendSubscriptionToN8n(subscription);
            
            Toast.makeText(this, "Suscripción guardada exitosamente", Toast.LENGTH_SHORT).show();
            finish();
            
        } catch (NumberFormatException e) {
            etSubAmount.setError("Ingresa un monto válido");
        }
    }
    
    /**
     * Envía los datos de la suscripción a n8n
     */
    private void sendSubscriptionToN8n(Subscription subscription) {
        try {
            SessionManager sessionManager = new SessionManager(this);
            String userName = sessionManager.getUsername();
            String userEmail = sessionManager.getEmail();
            
            // Obtener fecha de inicio (fecha actual)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            String fechaInicio = sdf.format(new java.util.Date());
            
            // Obtener fecha de vencimiento
            String fechaVencimiento = sdf.format(new java.util.Date(subscription.getNextRenewalDate()));
            
            // Formatear precio con símbolo de moneda
            String precio = subscription.getCurrency() + subscription.getAmount();
            
            // Crear payload con el formato solicitado
            Map<String, Object> subscriptionData = new HashMap<>();
            subscriptionData.put("herramienta", "suscripciones");
            subscriptionData.put("cliente", userName != null ? userName : "Usuario");
            subscriptionData.put("email", userEmail != null ? userEmail : "no-email@example.com");
            subscriptionData.put("plan", subscription.getName());
            subscriptionData.put("fecha_inicio", fechaInicio);
            subscriptionData.put("fecha_vencimiento", fechaVencimiento);
            subscriptionData.put("precio", precio);
            subscriptionData.put("frecuencia", subscription.getFrequency());
            
            webhookClient.sendEventToWebhook(
                N8N_SUBSCRIPTION_WEBHOOK_URL,
                "subscription_added",
                subscriptionData,
                String.valueOf(sessionManager.getUserId()),
                new N8nWebhookClient.WebhookCallback() {
                    @Override
                    public void onSuccess(N8nWebhookService.N8nResponse response) {
                        Log.d(TAG, "✅ Suscripción enviada a n8n exitosamente");
                        runOnUiThread(() -> 
                            Toast.makeText(AddSubscriptionActivity.this, 
                                "📊 Suscripción sincronizada con n8n", Toast.LENGTH_SHORT).show()
                        );
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ Error al enviar suscripción a n8n: " + error);
                        runOnUiThread(() -> 
                            Toast.makeText(AddSubscriptionActivity.this, 
                                "⚠️ Error al sincronizar: " + error, Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            );
        } catch (Exception e) {
            Log.e(TAG, "Error al enviar suscripción a n8n", e);
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
