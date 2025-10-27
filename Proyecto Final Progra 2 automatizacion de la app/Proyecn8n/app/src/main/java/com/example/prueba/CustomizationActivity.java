package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomizationActivity extends AppCompatActivity {

    private AvatarView avatarView;
    
    // Hair buttons
    private Button btnHairDefault;
    private Button btnHair01;
    private Button btnHairNone;
    
    // Shirt buttons
    private Button btnShirtDefault;
    private Button btnShirt01;
    private Button btnShirtNone;
    
    // Accessory buttons
    private Button btnAccessorySunglasses;
    private Button btnAccessoryNone;
    
    // Action buttons
    private Button btnResetAvatar;
    private Button btnSaveAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customization);
        
        // Initialize views
        initializeViews();
        
        // Setup button listeners
        setupButtonListeners();
        
        // Apply default configuration
        avatarView.setDefaultConfiguration();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        try {
            // Avatar view
            avatarView = findViewById(R.id.avatar_view);
            
            // Hair buttons
            btnHairDefault = findViewById(R.id.btn_hair_default);
            btnHair01 = findViewById(R.id.btn_hair_01);
            btnHairNone = findViewById(R.id.btn_hair_none);
            
            // Shirt buttons
            btnShirtDefault = findViewById(R.id.btn_shirt_default);
            btnShirt01 = findViewById(R.id.btn_shirt_01);
            btnShirtNone = findViewById(R.id.btn_shirt_none);
            
            // Accessory buttons
            btnAccessorySunglasses = findViewById(R.id.btn_accessory_sunglasses);
            btnAccessoryNone = findViewById(R.id.btn_accessory_none);
            
            // Action buttons
            btnResetAvatar = findViewById(R.id.btn_reset_avatar);
            btnSaveAvatar = findViewById(R.id.btn_save_avatar);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupButtonListeners() {
        try {
            // Hair button listeners
            btnHairDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setHair("hair_default");
                    showToast("Peinado aplicado: Default 💇‍♂️");
                }
            });
            
            btnHair01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setHair("hair_01");
                    showToast("Peinado aplicado: Spiky Azul 🔥");
                }
            });
            
            btnHairNone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setHair("none");
                    showToast("Peinado removido ✂️");
                }
            });
            
            // Shirt button listeners
            btnShirtDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setShirt("shirt_default");
                    showToast("Camiseta aplicada: Blanca 👕");
                }
            });
            
            btnShirt01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setShirt("shirt_01");
                    showToast("Camiseta aplicada: Roja con Rayas 🔴");
                }
            });
            
            btnShirtNone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setShirt("none");
                    showToast("Camiseta removida 👕");
                }
            });
            
            // Accessory button listeners
            btnAccessorySunglasses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setAccessory("accessory_sunglasses");
                    showToast("Accesorio aplicado: Gafas de Sol 🕶️");
                }
            });
            
            btnAccessoryNone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setAccessory("none");
                    showToast("Accesorios removidos 🚫");
                }
            });
            
            // Action button listeners
            btnResetAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avatarView.setDefaultConfiguration();
                    showToast("Avatar reseteado a configuración por defecto 🔄");
                }
            });
            
            btnSaveAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Aquí se guardaría la configuración del avatar
                    // Por ahora solo mostramos un mensaje
                    showToast("¡Avatar guardado exitosamente! 💾");
                    
                    // Opcional: regresar a la actividad anterior
                    // finish();
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Opcional: mostrar diálogo de confirmación antes de salir
        finish();
    }
}
