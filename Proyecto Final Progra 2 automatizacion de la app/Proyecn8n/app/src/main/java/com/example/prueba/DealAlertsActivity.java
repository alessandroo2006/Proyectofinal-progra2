package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DealAlertsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerDealAlerts;
    private DealAlertAdapter adapter;
    private FloatingActionButton fabAddDealAlert;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deal_alerts);
        
        // Initialize views
        initializeViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
        
        // Load sample data
        loadSampleData();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        recyclerDealAlerts = findViewById(R.id.recycler_deal_alerts);
        fabAddDealAlert = findViewById(R.id.fab_add_deal_alert);
    }
    
    private void setupRecyclerView() {
        adapter = new DealAlertAdapter(new DealAlertAdapter.OnDealAlertClickListener() {
            @Override
            public void onDealAlertClick(DealAlert dealAlert) {
                // TODO: Implement edit deal alert functionality
                Toast.makeText(DealAlertsActivity.this, 
                    "Editar: " + dealAlert.getKeywords(), Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onDealAlertLongClick(DealAlert dealAlert) {
                // Delete deal alert on long click
                adapter.removeDealAlert(dealAlert);
                Toast.makeText(DealAlertsActivity.this, 
                    "Alerta eliminada", Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerDealAlerts.setLayoutManager(new LinearLayoutManager(this));
        recyclerDealAlerts.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        fabAddDealAlert.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddDealAlertActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadSampleData() {
        List<DealAlert> sampleAlerts = new ArrayList<>();
        
        DealAlert alert1 = new DealAlert();
        alert1.setKeywords("Silla Gamer");
        alert1.setMaxPrice(500.0);
        alert1.setSources("Facebook Marketplace, MercadoLibre");
        alert1.setActive(true);
        sampleAlerts.add(alert1);
        
        DealAlert alert2 = new DealAlert();
        alert2.setKeywords("iPhone, Samsung Galaxy");
        alert2.setMaxPrice(1000.0);
        alert2.setSources("Tiendas Online, Marketplace");
        alert2.setActive(true);
        sampleAlerts.add(alert2);
        
        DealAlert alert3 = new DealAlert();
        alert3.setKeywords("Laptop Gaming");
        alert3.setMaxPrice(2000.0);
        alert3.setSources("Amazon, eBay");
        alert3.setActive(false);
        sampleAlerts.add(alert3);
        
        adapter.setDealAlerts(sampleAlerts);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
