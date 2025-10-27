package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExpenseClassifierSetupActivity extends AppCompatActivity {
    
    private TextView btnConnectGmail;
    private TextView btnConnectOutlook;
    private TextView txtConnectionStatus;
    private Switch switchAutoClassify;
    
    private boolean isGmailConnected = false;
    private boolean isOutlookConnected = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_classifier_setup);
        
        // Initialize views
        initializeViews();
        
        // Setup click listeners
        setupClickListeners();
        
        // Update connection status
        updateConnectionStatus();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        btnConnectGmail = findViewById(R.id.btn_connect_gmail);
        btnConnectOutlook = findViewById(R.id.btn_connect_outlook);
        txtConnectionStatus = findViewById(R.id.txt_connection_status);
        switchAutoClassify = findViewById(R.id.switch_auto_classify);
    }
    
    private void setupClickListeners() {
        btnConnectGmail.setOnClickListener(v -> {
            if (isGmailConnected) {
                disconnectGmail();
            } else {
                connectGmail();
            }
        });
        
        btnConnectOutlook.setOnClickListener(v -> {
            if (isOutlookConnected) {
                disconnectOutlook();
            } else {
                connectOutlook();
            }
        });
        
        switchAutoClassify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!isGmailConnected && !isOutlookConnected) {
                    Toast.makeText(this, "Conecta al menos un servicio de email primero", Toast.LENGTH_SHORT).show();
                    switchAutoClassify.setChecked(false);
                } else {
                    Toast.makeText(this, "Clasificación automática activada", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Clasificación automática desactivada", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void connectGmail() {
        // TODO: Implement OAuth2 flow for Gmail
        Toast.makeText(this, "Conectando con Gmail...", Toast.LENGTH_SHORT).show();
        
        // Simulate connection
        isGmailConnected = true;
        updateConnectionStatus();
        updateButtonText();
        
        Toast.makeText(this, "Conectado con Gmail exitosamente", Toast.LENGTH_SHORT).show();
    }
    
    private void disconnectGmail() {
        isGmailConnected = false;
        updateConnectionStatus();
        updateButtonText();
        
        if (switchAutoClassify.isChecked()) {
            switchAutoClassify.setChecked(false);
        }
        
        Toast.makeText(this, "Desconectado de Gmail", Toast.LENGTH_SHORT).show();
    }
    
    private void connectOutlook() {
        // TODO: Implement OAuth2 flow for Outlook
        Toast.makeText(this, "Conectando con Outlook...", Toast.LENGTH_SHORT).show();
        
        // Simulate connection
        isOutlookConnected = true;
        updateConnectionStatus();
        updateButtonText();
        
        Toast.makeText(this, "Conectado con Outlook exitosamente", Toast.LENGTH_SHORT).show();
    }
    
    private void disconnectOutlook() {
        isOutlookConnected = false;
        updateConnectionStatus();
        updateButtonText();
        
        if (switchAutoClassify.isChecked()) {
            switchAutoClassify.setChecked(false);
        }
        
        Toast.makeText(this, "Desconectado de Outlook", Toast.LENGTH_SHORT).show();
    }
    
    private void updateConnectionStatus() {
        if (isGmailConnected && isOutlookConnected) {
            txtConnectionStatus.setText("Conectado como: usuario@gmail.com (Gmail) y usuario@outlook.com (Outlook)");
        } else if (isGmailConnected) {
            txtConnectionStatus.setText("Conectado como: usuario@gmail.com (Gmail)");
        } else if (isOutlookConnected) {
            txtConnectionStatus.setText("Conectado como: usuario@outlook.com (Outlook)");
        } else {
            txtConnectionStatus.setText("No conectado");
        }
    }
    
    private void updateButtonText() {
        btnConnectGmail.setText(isGmailConnected ? "Desconectar Gmail" : "Conectar con Gmail");
        btnConnectOutlook.setText(isOutlookConnected ? "Desconectar Outlook" : "Conectar con Outlook");
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
