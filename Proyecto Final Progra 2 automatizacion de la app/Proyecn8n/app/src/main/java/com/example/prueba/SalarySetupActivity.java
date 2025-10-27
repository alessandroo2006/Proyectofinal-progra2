package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SalarySetupActivity extends AppCompatActivity {

    private EditText editSalary;
    private Button btnContinue;
    private TextView txtTitle;
    private TextView txtDescription;
    private UserPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_setup);

        // Initialize views
        initializeViews();
        
        // Initialize preferences manager
        preferencesManager = new UserPreferencesManager(this);
        
        // Setup UI
        setupUI();
        
        // Setup button click listener
        setupButtonListener();
    }

    private void initializeViews() {
        editSalary = findViewById(R.id.edit_salary);
        btnContinue = findViewById(R.id.btn_continue);
        txtTitle = findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_description);
    }

    private void setupUI() {
        txtTitle.setText("Configuración Inicial");
        txtDescription.setText("Para ayudarte a administrar mejor tus finanzas, necesitamos conocer tu salario mensual.");
        
        // Set input type for currency
        editSalary.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editSalary.setHint("Ejemplo: 5000.00");
        
        btnContinue.setText("Continuar");
    }

    private void setupButtonListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleContinue();
            }
        });
    }

    private void handleContinue() {
        String salaryText = editSalary.getText().toString().trim();
        
        if (salaryText.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu salario mensual", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            
            if (salary <= 0) {
                Toast.makeText(this, "El salario debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save salary
            preferencesManager.setMonthlySalary(salary);
            preferencesManager.setFirstTimeUser(false);

            // Navigate to category setup
            Intent intent = new Intent(this, CategorySetupActivity.class);
            intent.putExtra("monthly_salary", salary);
            
            // Pass user data if available (for new users from registration)
            Intent currentIntent = getIntent();
            if (currentIntent != null) {
                String username = currentIntent.getStringExtra("username");
                String email = currentIntent.getStringExtra("email");
                boolean isNewUser = currentIntent.getBooleanExtra("is_new_user", false);
                
                if (username != null) intent.putExtra("username", username);
                if (email != null) intent.putExtra("email", email);
                if (isNewUser) intent.putExtra("is_new_user", isNewUser);
            }
            
            startActivity(intent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, ingresa un valor numérico válido", Toast.LENGTH_SHORT).show();
        }
    }
}
