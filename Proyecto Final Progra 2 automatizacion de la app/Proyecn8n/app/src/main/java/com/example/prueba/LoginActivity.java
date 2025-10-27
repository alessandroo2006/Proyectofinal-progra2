package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsuario;
    private EditText editContrasena;
    private Button btnIniciarSesion;
    private TextView btnRegistrarse;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        editUsuario = findViewById(R.id.edit_usuario);
        editContrasena = findViewById(R.id.edit_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_iniciar_sesion);
        btnRegistrarse = findViewById(R.id.btn_registrarse);

        // Initialize repositories
        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);
        
        // Create test user if none exists
        createTestUserIfNeeded();
        
        // Check for setup completion message
        handleSetupCompletionMessage();

        // Set click listeners
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Setup session timeout callback
        sessionManager.setSessionTimeoutCallback(new SessionManager.SessionTimeoutCallback() {
            @Override
            public void onSessionTimeout() {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Sesión expirada. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
                    sessionManager.logoutUser();
                });
            }
        });
    }

    private void handleLogin() {
        // Get text from both fields
        String usuario = editUsuario.getText().toString().trim();
        String contrasena = editContrasena.getText().toString().trim();

        // Validate that fields are not empty
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        btnIniciarSesion.setEnabled(false);
        btnIniciarSesion.setText("Iniciando sesión...");

        // Authenticate user
        userRepository.loginUser(usuario, contrasena, new UserRepository.LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                runOnUiThread(() -> {
                    // Save user session with timeout
                    sessionManager.createLoginSession(user.getId(), user.getUsername(), user.getEmail());
                    
                    // Check if it's first time user
                    UserPreferencesManager preferencesManager = new UserPreferencesManager(LoginActivity.this);
                    Intent intent;
                    
                    if (preferencesManager.isFirstTimeUser()) {
                        // Navigate to salary setup for first time users
                        intent = new Intent(LoginActivity.this, SalarySetupActivity.class);
                    } else {
                        // Navigate to MainActivity for returning users
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("user_id", user.getId());
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("full_name", user.getFullName());
                        intent.putExtra("email", user.getEmail());
                    }
                    
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onLoginFailure(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    btnIniciarSesion.setEnabled(true);
                    btnIniciarSesion.setText("Iniciar Sesión");
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            UserPreferencesManager preferencesManager = new UserPreferencesManager(this);
            Intent intent;
            
            if (preferencesManager.isFirstTimeUser()) {
                // Navigate to salary setup for first time users
                intent = new Intent(this, SalarySetupActivity.class);
            } else {
                // Navigate to MainActivity for returning users
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("username", sessionManager.getUsername());
                intent.putExtra("email", sessionManager.getEmail());
            }
            
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManager.onDestroy();
    }
    
    private void createTestUserIfNeeded() {
        userRepository.checkUsernameExists("test", new UserRepository.UsernameCheckCallback() {
            @Override
            public void onUsernameCheck(boolean exists) {
                if (!exists) {
                    // Create test user
                    userRepository.registerUser("test", "123456", "test@example.com", new UserRepository.RegisterCallback() {
                        @Override
                        public void onRegisterSuccess(User user) {
                            // Test user created successfully
                        }
                        
                        @Override
                        public void onRegisterFailure(String message) {
                            // Log error but don't crash
                        }
                    });
                }
            }
        });
    }
    
    private void handleSetupCompletionMessage() {
        Intent intent = getIntent();
        if (intent != null) {
            boolean setupCompleted = intent.getBooleanExtra("setup_completed", false);
            String message = intent.getStringExtra("message");
            String username = intent.getStringExtra("username");
            
            if (setupCompleted && message != null) {
                // Show setup completion message
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                
                // Pre-fill username if available
                if (username != null && !username.isEmpty() && editUsuario != null) {
                    editUsuario.setText(username);
                }
            }
        }
    }
}