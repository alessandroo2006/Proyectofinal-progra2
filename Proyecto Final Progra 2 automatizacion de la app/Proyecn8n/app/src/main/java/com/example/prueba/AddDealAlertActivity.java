package com.example.prueba;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class AddDealAlertActivity extends AppCompatActivity {
    
    private TextInputEditText etAlertKeywords;
    private TextInputEditText etAlertMaxPrice;
    private CheckBox checkboxFacebookMarketplace;
    private CheckBox checkboxMercadoLibre;
    private CheckBox checkboxAmazon;
    private CheckBox checkboxRSSFeed;
    private Button btnSaveAlert;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_deal_alert);
        
        // Initialize views
        initializeViews();
        
        // Setup click listeners
        setupClickListeners();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        etAlertKeywords = findViewById(R.id.et_alert_keywords);
        etAlertMaxPrice = findViewById(R.id.et_alert_max_price);
        checkboxFacebookMarketplace = findViewById(R.id.checkbox_facebook_marketplace);
        checkboxMercadoLibre = findViewById(R.id.checkbox_mercado_libre);
        checkboxAmazon = findViewById(R.id.checkbox_amazon);
        checkboxRSSFeed = findViewById(R.id.checkbox_rss_feed);
        btnSaveAlert = findViewById(R.id.btn_save_alert);
    }
    
    private void setupClickListeners() {
        btnSaveAlert.setOnClickListener(v -> saveAlert());
    }
    
    private void saveAlert() {
        String keywords = etAlertKeywords.getText().toString().trim();
        String maxPriceText = etAlertMaxPrice.getText().toString().trim();
        
        // Validate inputs
        if (keywords.isEmpty()) {
            etAlertKeywords.setError("Las palabras clave son requeridas");
            return;
        }
        
        // Check if at least one source is selected
        boolean hasSource = checkboxFacebookMarketplace.isChecked() ||
                           checkboxMercadoLibre.isChecked() ||
                           checkboxAmazon.isChecked() ||
                           checkboxRSSFeed.isChecked();
        
        if (!hasSource) {
            Toast.makeText(this, "Selecciona al menos una fuente", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double maxPrice = maxPriceText.isEmpty() ? 0.0 : Double.parseDouble(maxPriceText);
            
            // Build sources string
            StringBuilder sources = new StringBuilder();
            if (checkboxFacebookMarketplace.isChecked()) {
                sources.append("Facebook Marketplace");
            }
            if (checkboxMercadoLibre.isChecked()) {
                if (sources.length() > 0) sources.append(", ");
                sources.append("MercadoLibre");
            }
            if (checkboxAmazon.isChecked()) {
                if (sources.length() > 0) sources.append(", ");
                sources.append("Amazon");
            }
            if (checkboxRSSFeed.isChecked()) {
                if (sources.length() > 0) sources.append(", ");
                sources.append("RSS Feed de Chollos");
            }
            
            // Create deal alert
            DealAlert dealAlert = new DealAlert();
            dealAlert.setKeywords(keywords);
            dealAlert.setMaxPrice(maxPrice);
            dealAlert.setSources(sources.toString());
            dealAlert.setActive(true);
            
            // TODO: Save to database or send to parent activity
            Toast.makeText(this, "Alerta guardada exitosamente", Toast.LENGTH_SHORT).show();
            finish();
            
        } catch (NumberFormatException e) {
            etAlertMaxPrice.setError("Ingresa un precio válido");
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
