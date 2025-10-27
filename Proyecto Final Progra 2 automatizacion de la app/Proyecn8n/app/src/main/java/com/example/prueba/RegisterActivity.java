package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUsuario;
    private EditText editEmail;
    private EditText editContrasena;
    private EditText editConfirmarContrasena;
    private Button btnRegistrarse;
    private Button btnCancelar;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        editUsuario = findViewById(R.id.edit_usuario);
        editEmail = findViewById(R.id.edit_email);
        editContrasena = findViewById(R.id.edit_contrasena);
        editConfirmarContrasena = findViewById(R.id.edit_confirmar_contrasena);
        btnRegistrarse = findViewById(R.id.btn_registrarse);
        btnCancelar = findViewById(R.id.btn_cancelar);

        // Initialize repository
        userRepository = new UserRepository(this);
        
        // Setup autofill
        AutofillHelper.setupAutofillForRegister(editUsuario, editEmail, editContrasena, editConfirmarContrasena);

        // Set click listeners
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleRegister() {
        // Get text from all fields
        String usuario = editUsuario.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String contrasena = editContrasena.getText().toString().trim();
        String confirmarContrasena = editConfirmarContrasena.getText().toString().trim();

        // Validate fields
        if (usuario.isEmpty() || email.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuario.length() < 3) {
            Toast.makeText(this, "El usuario debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Ingrese un email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasena.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        btnRegistrarse.setEnabled(false);
        btnRegistrarse.setText("Registrando...");

        // Register user
        userRepository.registerUser(usuario, contrasena, email, new UserRepository.RegisterCallback() {
            @Override
            public void onRegisterSuccess(User user) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                    
                    // Mark user as first time user and navigate to salary setup
                    UserPreferencesManager preferencesManager = new UserPreferencesManager(RegisterActivity.this);
                    preferencesManager.setFirstTimeUser(true);
                    
                    // Navigate directly to salary setup for new users
                    Intent intent = new Intent(RegisterActivity.this, SalarySetupActivity.class);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("is_new_user", true);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onRegisterFailure(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    btnRegistrarse.setEnabled(true);
                    btnRegistrarse.setText("Registrarse");
                });
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
