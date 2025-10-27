package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionTrackerActivity extends AppCompatActivity {
    
    private static final String TAG = "SubscriptionTrackerActivity";
    // URL unificada con el resto de la app (foxyti en lugar de userfox)
    private static final String N8N_SUBSCRIPTION_WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6";
    
    private SubscriptionViewModel viewModel;
    private RecyclerView recyclerSubscriptions;
    private SubscriptionAdapter adapter;
    private FloatingActionButton fabAddSubscription;
    private N8nWebhookClient webhookClient;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subscription_tracker);
        
        // Initialize webhook client
        webhookClient = N8nWebhookClient.getInstance();
        
        // Initialize views
        initializeViews();
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SubscriptionViewModel.class);
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup observers
        setupObservers();
        
        // Setup click listeners
        setupClickListeners();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        recyclerSubscriptions = findViewById(R.id.recycler_subscriptions);
        fabAddSubscription = findViewById(R.id.fab_add_subscription);
    }
    
    private void setupRecyclerView() {
        adapter = new SubscriptionAdapter(new SubscriptionAdapter.OnSubscriptionClickListener() {
            @Override
            public void onSubscriptionClick(Subscription subscription) {
                // TODO: Implement edit subscription functionality
                Toast.makeText(SubscriptionTrackerActivity.this, 
                    "Editar: " + subscription.getName(), Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onSubscriptionLongClick(Subscription subscription) {
                // Delete subscription on long click
                viewModel.delete(subscription);
                
                // Send deletion event to n8n
                sendSubscriptionDeletedToN8n(subscription);
                
                Toast.makeText(SubscriptionTrackerActivity.this, 
                    "Suscripción eliminada", Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerSubscriptions.setLayoutManager(new LinearLayoutManager(this));
        recyclerSubscriptions.setAdapter(adapter);
    }
    
    private void setupObservers() {
        viewModel.getAllSubscriptions().observe(this, subscriptions -> {
            adapter.setSubscriptions(subscriptions);
        });
        
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void setupClickListeners() {
        fabAddSubscription.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddSubscriptionActivity.class);
            startActivity(intent);
        });
    }
    
    /**
     * Envía evento de eliminación de suscripción a n8n
     */
    private void sendSubscriptionDeletedToN8n(Subscription subscription) {
        try {
            String userId = String.valueOf(new SessionManager(this).getUserId());
            
            // Crear payload con los datos de la suscripción eliminada
            Map<String, Object> subscriptionData = new HashMap<>();
            subscriptionData.put("subscription_name", subscription.getName());
            subscriptionData.put("amount", subscription.getAmount());
            subscriptionData.put("currency", subscription.getCurrency());
            subscriptionData.put("frequency", subscription.getFrequency());
            subscriptionData.put("action_type", "subscription_deleted");
            subscriptionData.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            webhookClient.sendEventToWebhook(
                N8N_SUBSCRIPTION_WEBHOOK_URL,
                "subscription_deleted",
                subscriptionData,
                userId,
                new N8nWebhookClient.WebhookCallback() {
                    @Override
                    public void onSuccess(N8nWebhookService.N8nResponse response) {
                        Log.d(TAG, "✅ Eliminación de suscripción enviada a n8n");
                    }
                    
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "❌ Error al enviar eliminación a n8n: " + error);
                    }
                }
            );
        } catch (Exception e) {
            Log.e(TAG, "Error al enviar eliminación de suscripción a n8n", e);
        }
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
