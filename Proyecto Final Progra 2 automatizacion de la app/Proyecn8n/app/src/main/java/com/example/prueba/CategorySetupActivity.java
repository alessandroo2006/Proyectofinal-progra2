package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class CategorySetupActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtTotalBudget;
    private TextView txtRemainingBudget;
    private LinearLayout categoriesContainer;
    private Button btnFinish;
    
    private UserPreferencesManager preferencesManager;
    private double monthlySalary;
    private double totalAllocatedBudget = 0.0;
    
    // Category edit texts
    private EditText editAlimentos;
    private EditText editSuscripciones;
    private EditText editJuegos;
    private EditText editFamilia;
    private EditText editEmergencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_setup);

        // Get salary from intent
        monthlySalary = getIntent().getDoubleExtra("monthly_salary", 0.0);
        
        // Initialize views and preferences
        initializeViews();
        preferencesManager = new UserPreferencesManager(this);
        
        // Setup UI
        setupUI();
        
        // Setup button listener
        setupButtonListener();
    }

    private void initializeViews() {
        txtTitle = findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_description);
        txtTotalBudget = findViewById(R.id.txt_total_budget);
        txtRemainingBudget = findViewById(R.id.txt_remaining_budget);
        categoriesContainer = findViewById(R.id.categories_container);
        btnFinish = findViewById(R.id.btn_finish);
        
        // Initialize category edit texts
        editAlimentos = findViewById(R.id.edit_alimentos);
        editSuscripciones = findViewById(R.id.edit_suscripciones);
        editJuegos = findViewById(R.id.edit_juegos);
        editFamilia = findViewById(R.id.edit_familia);
        editEmergencias = findViewById(R.id.edit_emergencias);
    }

    private void setupUI() {
        txtTitle.setText("Configuración de Categorías");
        txtDescription.setText("Distribuye tu salario mensual entre las diferentes categorías de gastos:");
        
        // Set input types for currency
        editAlimentos.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editSuscripciones.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editJuegos.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editFamilia.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editEmergencias.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        
        // Set hints
        editAlimentos.setHint("0.00");
        editSuscripciones.setHint("0.00");
        editJuegos.setHint("0.00");
        editFamilia.setHint("0.00");
        editEmergencias.setHint("0.00");
        
        // Update budget display
        updateBudgetDisplay();
        
        // Add text change listeners to update totals
        setupTextChangeListeners();
        
        btnFinish.setText("Finalizar Configuración");
    }

    private void setupTextChangeListeners() {
        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateBudgetDisplay();
                }
            }
        };

        editAlimentos.setOnFocusChangeListener(focusListener);
        editSuscripciones.setOnFocusChangeListener(focusListener);
        editJuegos.setOnFocusChangeListener(focusListener);
        editFamilia.setOnFocusChangeListener(focusListener);
        editEmergencias.setOnFocusChangeListener(focusListener);
    }

    private void updateBudgetDisplay() {
        try {
            double alimentos = getDoubleValue(editAlimentos.getText().toString());
            double suscripciones = getDoubleValue(editSuscripciones.getText().toString());
            double juegos = getDoubleValue(editJuegos.getText().toString());
            double familia = getDoubleValue(editFamilia.getText().toString());
            double emergencias = getDoubleValue(editEmergencias.getText().toString());
            
            totalAllocatedBudget = alimentos + suscripciones + juegos + familia + emergencias;
            double remaining = monthlySalary - totalAllocatedBudget;
            
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "GT"));
            formatter.setCurrency(java.util.Currency.getInstance("GTQ"));
            
            txtTotalBudget.setText("Total Asignado: " + formatter.format(totalAllocatedBudget));
            
            if (remaining >= 0) {
                txtRemainingBudget.setText("Restante: " + formatter.format(remaining));
                txtRemainingBudget.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                txtRemainingBudget.setText("Exceso: " + formatter.format(Math.abs(remaining)));
                txtRemainingBudget.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
            
        } catch (Exception e) {
            txtTotalBudget.setText("Total Asignado: Q 0.00");
            txtRemainingBudget.setText("Restante: " + NumberFormat.getCurrencyInstance(new Locale("es", "GT")).format(monthlySalary));
        }
    }

    private double getDoubleValue(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void setupButtonListener() {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinish();
            }
        });
    }

    private void handleFinish() {
        try {
            // Get values from edit texts
            double alimentos = getDoubleValue(editAlimentos.getText().toString());
            double suscripciones = getDoubleValue(editSuscripciones.getText().toString());
            double juegos = getDoubleValue(editJuegos.getText().toString());
            double familia = getDoubleValue(editFamilia.getText().toString());
            double emergencias = getDoubleValue(editEmergencias.getText().toString());
            
            // Validate that total doesn't exceed salary
            double total = alimentos + suscripciones + juegos + familia + emergencias;
            if (total > monthlySalary) {
                Toast.makeText(this, "El total de gastos no puede exceder tu salario mensual", Toast.LENGTH_LONG).show();
                return;
            }
            
            // Save category budgets
            preferencesManager.setCategoryBudget("alimentos", alimentos);
            preferencesManager.setCategoryBudget("suscripciones", suscripciones);
            preferencesManager.setCategoryBudget("juegos", juegos);
            preferencesManager.setCategoryBudget("familia", familia);
            preferencesManager.setCategoryBudget("emergencias", emergencias);
            preferencesManager.setCategoriesConfigured(true);
            
            Toast.makeText(this, "Configuración completada exitosamente", Toast.LENGTH_SHORT).show();
            
            // Check if this is a new user from registration
            boolean isNewUser = getIntent().getBooleanExtra("is_new_user", false);
            
            if (isNewUser) {
                // For new users, we need to log them in first
                // Get user data from intent
                String username = getIntent().getStringExtra("username");
                String email = getIntent().getStringExtra("email");
                
                if (username != null && !username.isEmpty()) {
                    // Create session for the new user
                    SessionManager sessionManager = new SessionManager(this);
                    // Note: We can't get the password here, so we'll need to handle this differently
                    // For now, let's navigate to login with a message
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("setup_completed", true);
                    intent.putExtra("username", username);
                    intent.putExtra("message", "Configuración completada. Por favor, inicia sesión con tu nueva cuenta.");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Fallback: navigate to login
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("setup_completed", true);
                    intent.putExtra("message", "Configuración completada. Por favor, inicia sesión.");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            } else {
                // For existing users, navigate directly to MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar la configuración", Toast.LENGTH_SHORT).show();
        }
    }
}
