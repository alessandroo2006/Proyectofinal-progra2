package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Locale;

public class PresupuestoActivity extends AppCompatActivity {

    // Navigation items
    private LinearLayout navInicio;
    private LinearLayout navAnalisis;
    private LinearLayout navSuenos;
    private LinearLayout navCoach;
    private LinearLayout navPresupuesto;

    // AhorroAvatar components
    private TextView txtTotalSaved;
    private TextView txtMotivationalMessage;
    private EditText etSavingsAmount;
    private Button btnAddSavings;
    private Button btnViewCloset;
    private LinearLayout missionsContainer;
    private LinearLayout avatarPreviewContainer;
    private TextView avatarDisplay;
    private TextView avatarItemsCount;
    
    private AhorroAvatarManager avatarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_presupuesto);
        
        // Initialize views
        initializeViews();
        
        // Initialize avatar manager
        avatarManager = new AhorroAvatarManager(this);
        
        // Setup navigation
        setupNavigation();
        
        // Setup AhorroAvatar functionality
        setupAhorroAvatar();
        
        // Load current state
        loadCurrentState();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        try {
            // Navigation views
            navInicio = findViewById(R.id.nav_inicio);
            navAnalisis = findViewById(R.id.nav_analisis);
            navSuenos = findViewById(R.id.nav_suenos);
            navCoach = findViewById(R.id.nav_coach);
            navPresupuesto = findViewById(R.id.nav_presupuesto);

            // AhorroAvatar views
            txtTotalSaved = findViewById(R.id.txt_total_saved);
            txtMotivationalMessage = findViewById(R.id.txt_motivational_message);
            etSavingsAmount = findViewById(R.id.et_savings_amount);
            btnAddSavings = findViewById(R.id.btn_add_savings);
            btnViewCloset = findViewById(R.id.btn_view_closet);
            missionsContainer = findViewById(R.id.missions_container);
            avatarPreviewContainer = findViewById(R.id.avatar_preview_container);
            avatarDisplay = findViewById(R.id.avatar_display);
            avatarItemsCount = findViewById(R.id.avatar_items_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupAhorroAvatar() {
        try {
            // Add savings button
            if (btnAddSavings != null) {
                btnAddSavings.setOnClickListener(v -> addSavings());
            }
            
            // View closet button
            if (btnViewCloset != null) {
                btnViewCloset.setOnClickListener(v -> {
                    Intent intent = new Intent(this, CustomizationActivity.class);
                    startActivity(intent);
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addSavings() {
        try {
            String amountStr = etSavingsAmount.getText().toString().trim();
            if (amountStr.isEmpty()) {
                showCoachMessage("¡Hey! 💰 Escribe cuánto ahorraste para que pueda celebrar contigo! ✨");
                return;
            }
            
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showCoachMessage("¡Ups! 😅 El ahorro debe ser mayor a Q0. ¡Inténtalo de nuevo! 💪");
                return;
            }
            
            // Add savings to manager
            avatarManager.addSavings(amount);
            
            // Clear input
            etSavingsAmount.setText("");
            
            // Update UI
            updateProgressDisplay();
            updateMissionsDisplay();
            updateAvatarDisplay();
            
            // Show celebration message
            showCelebrationMessage(amount);
            
        } catch (NumberFormatException e) {
            showCoachMessage("¡Ups! 😅 Por favor escribe un número válido (ej: 25.50) 💡");
        } catch (Exception e) {
            e.printStackTrace();
            showCoachMessage("¡Ups! Algo salió mal. ¡Inténtalo de nuevo! 🔄");
        }
    }
    
    private void showCelebrationMessage(double amount) {
        double totalSaved = avatarManager.getTotalSaved();
        List<AvatarMission> newRewards = avatarManager.getAvailableMissions();
        
        // Check if any new rewards were unlocked
        if (!newRewards.isEmpty()) {
            for (AvatarMission reward : newRewards) {
                if (reward.isCompleted() && !reward.isClaimed()) {
                    showCoachMessage("¡FELICIDADES! 🎉 Has desbloqueado: " + 
                                   reward.getRewardName() + " " + reward.getRewardIcon() + 
                                   "\n👉 Ve a tu armario virtual para equiparlo!");
                    
                    // Claim the reward
                    avatarManager.claimReward(reward.getId());
                }
            }
        } else {
            // Regular motivational message
            String message = avatarManager.getMotivationalMessage();
            showCoachMessage(message);
        }
    }
    
    private void showCoachMessage(String message) {
        if (txtMotivationalMessage != null) {
            txtMotivationalMessage.setText(message);
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    private void loadCurrentState() {
        updateProgressDisplay();
        updateMissionsDisplay();
        updateAvatarDisplay();
        showCoachMessage(avatarManager.getMotivationalMessage());
    }
    
    private void updateProgressDisplay() {
        try {
            if (txtTotalSaved != null) {
                double totalSaved = avatarManager.getTotalSaved();
                String formattedAmount = String.format(Locale.getDefault(), "Q %,.2f", totalSaved);
                txtTotalSaved.setText(formattedAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateMissionsDisplay() {
        try {
            if (missionsContainer != null) {
                missionsContainer.removeAllViews();
                
                List<AvatarMission> missions = avatarManager.getMissions();
                for (AvatarMission mission : missions) {
                    createMissionCard(mission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createMissionCard(AvatarMission mission) {
        try {
            View missionCard = LayoutInflater.from(this).inflate(R.layout.mission_card_layout, null);
            
            TextView txtMissionName = missionCard.findViewById(R.id.mission_name);
            TextView txtMissionDescription = missionCard.findViewById(R.id.mission_description);
            TextView txtMissionReward = missionCard.findViewById(R.id.mission_reward);
            TextView txtMissionProgress = missionCard.findViewById(R.id.mission_progress);
            Button btnClaim = missionCard.findViewById(R.id.btn_claim_reward);
            
            // Set mission data
            txtMissionName.setText(mission.getName());
            txtMissionDescription.setText(mission.getDescription());
            txtMissionReward.setText(mission.getRewardName() + " " + mission.getRewardIcon());
            
            // Calculate progress
            double totalSaved = avatarManager.getTotalSaved();
            double progress = Math.min(totalSaved, mission.getTargetAmount());
            double progressPercent = (progress / mission.getTargetAmount()) * 100;
            
            txtMissionProgress.setText(String.format(Locale.getDefault(), 
                "Progreso: Q%.0f / Q%.0f (%.0f%%)", 
                progress, mission.getTargetAmount(), progressPercent));
            
            // Handle claim button
            if (mission.isCompleted() && !mission.isClaimed()) {
                btnClaim.setVisibility(View.VISIBLE);
                btnClaim.setOnClickListener(v -> {
                    avatarManager.claimReward(mission.getId());
                    updateMissionsDisplay();
                    updateAvatarDisplay();
                    showCoachMessage("¡Excelente! 🎁 Has reclamado " + mission.getRewardName() + 
                                   "! ¡Ve a equiparlo en tu avatar! 👕");
                });
            } else if (mission.isClaimed()) {
                btnClaim.setText("✅ Reclamado");
                btnClaim.setEnabled(false);
                btnClaim.setVisibility(View.VISIBLE);
            } else {
                btnClaim.setVisibility(View.GONE);
            }
            
            // Add to container
            missionsContainer.addView(missionCard);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateAvatarDisplay() {
        try {
            if (avatarDisplay != null && avatarItemsCount != null) {
                List<AvatarMission> claimedRewards = avatarManager.getClaimedRewards();
                int itemCount = claimedRewards.size();
                
                avatarItemsCount.setText(itemCount + " items desbloqueados");
                
                // Update avatar display based on equipped items
                // For now, just show a basic avatar
                if (itemCount > 0) {
                    avatarDisplay.setText("👤✨");
                } else {
                    avatarDisplay.setText("👤");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupNavigation() {
        try {
            if (navInicio != null) {
                navInicio.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navAnalisis != null) {
                navAnalisis.setOnClickListener(v -> {
                    Intent intent = new Intent(this, AnalisisActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navSuenos != null) {
                navSuenos.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MisSueñosActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navCoach != null) {
                navCoach.setOnClickListener(v -> {
                    Intent intent = new Intent(this, CoachActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
            
            if (navPresupuesto != null) {
                navPresupuesto.setOnClickListener(v -> {
                    // Already on this screen
                    setSelectedNavItem(navPresupuesto);
                });
            }
            
            // Set initial selection
            if (navPresupuesto != null) {
                setSelectedNavItem(navPresupuesto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setSelectedNavItem(LinearLayout selectedItem) {
        try {
            // Reset all navigation items
            if (navInicio != null) navInicio.setBackground(null);
            if (navAnalisis != null) navAnalisis.setBackground(null);
            if (navSuenos != null) navSuenos.setBackground(null);
            if (navCoach != null) navCoach.setBackground(null);
            if (navPresupuesto != null) navPresupuesto.setBackground(null);
            
            // Set selected item background
            if (selectedItem != null) {
                selectedItem.setBackgroundResource(R.drawable.nav_item_selected_background);
                updateNavTextColors(selectedItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateNavTextColors(LinearLayout selectedItem) {
        try {
            // Reset all text colors to default
            if (navInicio != null && navInicio.getChildCount() > 1) {
                TextView textInicio = (TextView) navInicio.getChildAt(1);
                textInicio.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navAnalisis != null && navAnalisis.getChildCount() > 1) {
                TextView textAnalisis = (TextView) navAnalisis.getChildAt(1);
                textAnalisis.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navSuenos != null && navSuenos.getChildCount() > 1) {
                TextView textSuenos = (TextView) navSuenos.getChildAt(1);
                textSuenos.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navCoach != null && navCoach.getChildCount() > 1) {
                TextView textCoach = (TextView) navCoach.getChildAt(1);
                textCoach.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            if (navPresupuesto != null && navPresupuesto.getChildCount() > 1) {
                TextView textPresupuesto = (TextView) navPresupuesto.getChildAt(1);
                textPresupuesto.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
            
            // Set selected text color
            if (selectedItem != null && selectedItem.getChildCount() > 1) {
                TextView selectedText = (TextView) selectedItem.getChildAt(1);
                selectedText.setTextColor(getResources().getColor(R.color.primary_blue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}