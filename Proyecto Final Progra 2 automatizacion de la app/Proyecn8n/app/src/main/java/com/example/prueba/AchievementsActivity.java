package com.example.prueba;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AchievementsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerAchievements;
    private AchievementAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_achievements);
        
        // Initialize views
        initializeViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load achievements data
        loadAchievementsData();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void initializeViews() {
        recyclerAchievements = findViewById(R.id.recycler_achievements);
    }
    
    private void setupRecyclerView() {
        adapter = new AchievementAdapter(new AchievementAdapter.OnAchievementClickListener() {
            @Override
            public void onAchievementClick(Achievement achievement) {
                if (achievement.isUnlocked()) {
                    Toast.makeText(AchievementsActivity.this, 
                        "Logro desbloqueado: " + achievement.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AchievementsActivity.this, 
                        "Logro bloqueado: " + achievement.getDescription(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerAchievements.setLayoutManager(layoutManager);
        recyclerAchievements.setAdapter(adapter);
    }
    
    private void loadAchievementsData() {
        List<Achievement> achievements = new ArrayList<>();
        
        // Unlocked achievements
        Achievement achievement1 = new Achievement();
        achievement1.setName("Primer Gasto");
        achievement1.setDescription("Registra tu primer gasto");
        achievement1.setUnlocked(true);
        achievement1.setIcon("🎯");
        achievements.add(achievement1);
        
        Achievement achievement2 = new Achievement();
        achievement2.setName("Racha de 3 Días");
        achievement2.setDescription("Registra gastos 3 días seguidos");
        achievement2.setUnlocked(true);
        achievement2.setIcon("🔥");
        achievements.add(achievement2);
        
        Achievement achievement3 = new Achievement();
        achievement3.setName("Ahorrador");
        achievement3.setDescription("Ahorra Q 1,000");
        achievement3.setUnlocked(true);
        achievement3.setIcon("💰");
        achievements.add(achievement3);
        
        // Locked achievements
        Achievement achievement4 = new Achievement();
        achievement4.setName("Racha de 7 Días");
        achievement4.setDescription("Registra gastos 7 días seguidos");
        achievement4.setUnlocked(false);
        achievement4.setIcon("🏆");
        achievements.add(achievement4);
        
        Achievement achievement5 = new Achievement();
        achievement5.setName("Gran Ahorrador");
        achievement5.setDescription("Ahorra Q 5,000");
        achievement5.setUnlocked(false);
        achievement5.setIcon("💎");
        achievements.add(achievement5);
        
        Achievement achievement6 = new Achievement();
        achievement6.setName("Experto en Presupuesto");
        achievement6.setDescription("Mantén tu presupuesto por 30 días");
        achievement6.setUnlocked(false);
        achievement6.setIcon("📊");
        achievements.add(achievement6);
        
        adapter.setAchievements(achievements);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
