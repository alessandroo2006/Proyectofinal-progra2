package com.example.prueba;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Updated to the webhook URL provided by the user
    private static final String FINANCIAL_TOOLS_WEBHOOK_URL = "https://foxyti.app.n8n.cloud/webhook-test/942559e5-d4e4-4ad6-a384-9fa8f4b0f2f6";

    private TextView txtGreeting;
    private TextView txtTotalBalance;
    private TextView txtBalanceChange;
    private TextView txtIncome;
    private TextView txtExpenses;
    private TextView txtSavings;
    private Button btnLogout;
    private Button btnTestWebhook;
    private DonutChartView donutChart;

    private double currentBalance = 12450.00;

    private LinearLayout navInicio, navAnalisis, navSuenos, navCoach, navPresupuesto;
    private UserPreferencesManager preferencesManager;
    private LinearLayout legendContainer;
    private SmartAlertsManager alertsManager;
    private LinearLayout alertsContainer;
    private N8nWebhookClient webhookClient;
    private androidx.cardview.widget.CardView btnGotoFinancialTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        preferencesManager = new UserPreferencesManager(this);
        alertsManager = new SmartAlertsManager(this);
        webhookClient = N8nWebhookClient.getInstance();

        updateUserGreeting();
        loadFinancialData();
        setupLogout();
        setupClickListeners();
        testWebhookConnection();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        try {
            txtGreeting = findViewById(R.id.txt_greeting);
            txtTotalBalance = findViewById(R.id.txt_total_balance);
            txtBalanceChange = findViewById(R.id.txt_balance_change);
            txtIncome = findViewById(R.id.txt_income);
            txtExpenses = findViewById(R.id.txt_expenses);
            txtSavings = findViewById(R.id.txt_savings);
            btnLogout = findViewById(R.id.btn_logout);
            btnTestWebhook = findViewById(R.id.btn_test_webhook);
            donutChart = findViewById(R.id.donut_chart);
            navInicio = findViewById(R.id.nav_inicio);
            navAnalisis = findViewById(R.id.nav_analisis);
            navSuenos = findViewById(R.id.nav_suenos);
            navCoach = findViewById(R.id.nav_coach);
            navPresupuesto = findViewById(R.id.nav_presupuesto);
            legendContainer = findViewById(R.id.legend_container);
            btnGotoFinancialTools = findViewById(R.id.btn_goto_financial_tools);
            alertsContainer = findViewById(R.id.alerts_container);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }

    private void loadFinancialData() {
        try {
            double monthlySalary = preferencesManager.getMonthlySalary();
            double[] categoryBudgets = preferencesManager.getAllCategoryBudgets();
            calculateAndDisplayFinancialData(monthlySalary, categoryBudgets);
            setupDonutChart();
            updateChartLegend();
            generateAndDisplaySmartAlerts();
            sendFinancialDataToWebhook();
        } catch (Exception e) {
            Log.e(TAG, "Error loading financial data", e);
            loadDefaultFinancialData();
        }
    }

    private void calculateAndDisplayFinancialData(double monthlySalary, double[] categoryBudgets) {
        try {
            currentBalance = monthlySalary;
            updateBalanceDisplay();
            double totalBudgetAllocated = 0;
            for (double budget : categoryBudgets) {
                totalBudgetAllocated += budget;
            }
            double income = monthlySalary;
            double expenses = totalBudgetAllocated;
            double savings = monthlySalary - totalBudgetAllocated;
            double percentageChange = 8.5;
            if (txtBalanceChange != null) txtBalanceChange.setText(String.format("+%.1f%% este mes", percentageChange));
            if (txtIncome != null) txtIncome.setText(String.format("Q %,.0f", income));
            if (txtExpenses != null) txtExpenses.setText(String.format("Q %,.0f", expenses));
            if (txtSavings != null) txtSavings.setText(String.format("Q %,.0f", savings));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultFinancialData() {
        try {
            currentBalance = 12450.00;
            updateBalanceDisplay();
            if (txtBalanceChange != null) txtBalanceChange.setText("+8.5% este mes");
            if (txtIncome != null) txtIncome.setText("Q 5,500");
            if (txtExpenses != null) txtExpenses.setText("Q 3,900");
            if (txtSavings != null) txtSavings.setText("Q 1,600");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupDonutChart() {
        try {
            if (donutChart != null) {
                double[] categoryBudgets = preferencesManager.getAllCategoryBudgets();
                double totalBudget = 0;
                for (double budget : categoryBudgets) {
                    totalBudget += budget;
                }
                List<Float> validPercentages = new ArrayList<>();
                List<Integer> validColors = new ArrayList<>();
                int[] colors = {0xFF3498DB, 0xFFF39C12, 0xFF9B59B6, 0xFF27AE60, 0xFFE74C3C};
                for (int i = 0; i < categoryBudgets.length; i++) {
                    if (categoryBudgets[i] > 0 && totalBudget > 0) {
                        validPercentages.add((float) ((categoryBudgets[i] / totalBudget) * 100));
                        validColors.add(colors[i]);
                    }
                }
                if (validPercentages.isEmpty()) {
                    float[] defaultData = {30f, 25f, 25f, 20f};
                    int[] defaultColors = {0xFF3498DB, 0xFF27AE60, 0xFFF39C12, 0xFF7F8C8D};
                    donutChart.setData(defaultData, defaultColors);
                } else {
                    float[] expensesData = new float[validPercentages.size()];
                    int[] finalColors = new int[validColors.size()];
                    for (int i = 0; i < validPercentages.size(); i++) {
                        expensesData[i] = validPercentages.get(i);
                        finalColors[i] = validColors.get(i);
                    }
                    donutChart.setData(expensesData, finalColors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateChartLegend() {
        try {
            if (legendContainer != null) {
                legendContainer.removeAllViews();
                double[] categoryBudgets = preferencesManager.getAllCategoryBudgets();
                String[] categoryNames = preferencesManager.getCategoryNames();
                int[] colors = {0xFF3498DB, 0xFFF39C12, 0xFF9B59B6, 0xFF27AE60, 0xFFE74C3C};
                for (int i = 0; i < categoryBudgets.length; i++) {
                    if (categoryBudgets[i] > 0) {
                        LinearLayout legendItem = (LinearLayout) getLayoutInflater().inflate(R.layout.legend_item, legendContainer, false);
                        View colorDot = legendItem.findViewById(R.id.legend_dot);
                        TextView legendText = legendItem.findViewById(R.id.legend_text);
                        colorDot.getBackground().setTint(colors[i]);
                        legendText.setText(capitalizeFirstLetter(categoryNames[i]));
                        legendContainer.addView(legendItem);
                    }
                }
                if (legendContainer.getChildCount() == 0) {
                    createDefaultLegend();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            createDefaultLegend();
        }
    }

    private void createDefaultLegend() {
        try {
            if (legendContainer != null) {
                legendContainer.removeAllViews();
                String[] defaultCategories = {"Alimentación", "Transporte", "Entretenimiento", "Otros"};
                int[] defaultColors = {0xFF3498DB, 0xFF27AE60, 0xFFF39C12, 0xFF7F8C8D};
                for (int i = 0; i < defaultCategories.length; i++) {
                    LinearLayout legendItem = (LinearLayout) getLayoutInflater().inflate(R.layout.legend_item, legendContainer, false);
                    View colorDot = legendItem.findViewById(R.id.legend_dot);
                    TextView legendText = legendItem.findViewById(R.id.legend_text);
                    colorDot.getBackground().setTint(defaultColors[i]);
                    legendText.setText(defaultCategories[i]);
                    legendContainer.addView(legendItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private void generateAndDisplaySmartAlerts() {
        try {
            if (alertsContainer != null && alertsManager != null) {
                alertsContainer.removeAllViews();
                List<SmartAlertsManager.SmartAlert> alerts = alertsManager.generateSmartAlerts();
                int maxAlerts = Math.min(alerts.size(), 3);
                for (int i = 0; i < maxAlerts; i++) {
                    createAlertCard(alerts.get(i));
                }
                if (alerts.isEmpty()) {
                    createNoAlertsMessage();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAlertCard(SmartAlertsManager.SmartAlert alert) {
        try {
            View alertCard = getLayoutInflater().inflate(R.layout.alert_card_layout, alertsContainer, false);
            View indicatorBar = alertCard.findViewById(R.id.alert_indicator);
            TextView messageText = alertCard.findViewById(R.id.alert_message);
            TextView categoryText = alertCard.findViewById(R.id.alert_category);
            if (indicatorBar != null) indicatorBar.setBackgroundColor(alert.color);
            if (messageText != null) messageText.setText(alert.message);
            if (categoryText != null) categoryText.setText(alert.category);
            alertsContainer.addView(alertCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNoAlertsMessage() {
        try {
            if (alertsContainer != null) {
                 TextView noAlertsText = new TextView(this);
                 noAlertsText.setText("¡Excelente! No hay alertas importantes en este momento.");
                 noAlertsText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                 noAlertsText.setGravity(android.view.Gravity.CENTER);
                 alertsContainer.addView(noAlertsText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBalanceDisplay() {
        try {
            if (txtTotalBalance != null) {
                txtTotalBalance.setText(String.format("Q %,.2f", currentBalance));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLogout() {
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> showLogoutDialog());
        }
        
        if (btnTestWebhook != null) {
            btnTestWebhook.setOnClickListener(v -> probarWebhookManual());
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Sí, cerrar sesión", (dialog, which) -> {
                new SessionManager(this).logoutUser();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void setupClickListeners() {
        try {
            navInicio.setOnClickListener(v -> setSelectedNavItem(navInicio));
            navAnalisis.setOnClickListener(v -> {
                startActivity(new Intent(this, AnalisisActivity.class));
                finish();
            });
            navSuenos.setOnClickListener(v -> {
                startActivity(new Intent(this, MisSueñosActivity.class));
                finish();
            });
            navCoach.setOnClickListener(v -> {
                startActivity(new Intent(this, CoachActivity.class));
                finish();
            });
            navPresupuesto.setOnClickListener(v -> {
                startActivity(new Intent(this, PresupuestoActivity.class));
                finish();
            });

            btnGotoFinancialTools.setOnClickListener(v -> {
                startActivity(new Intent(this, FinancialToolsActivity.class));
            });

            setSelectedNavItem(navInicio);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }

    private void setSelectedNavItem(LinearLayout selectedItem) {
        LinearLayout[] navItems = {navInicio, navAnalisis, navSuenos, navCoach, navPresupuesto};
        for (LinearLayout item : navItems) {
            if (item != null) {
                item.setBackground(null);
                TextView text = item.findViewWithTag("nav_text");
                if (text != null) {
                    text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    text.setTypeface(null, Typeface.NORMAL);
                }
            }
        }

        if (selectedItem != null) {
            selectedItem.setBackgroundResource(R.drawable.nav_item_selected_background);
            TextView text = selectedItem.findViewWithTag("nav_text");
            if (text != null) {
                text.setTextColor(getResources().getColor(R.color.primary_blue));
                text.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    private void updateNavTextColors(LinearLayout selectedItem) {
       // This logic is now handled in setSelectedNavItem, so this can be removed or left empty.
    }

    private void updateUserGreeting() {
        if (txtGreeting == null) return;
        String userName = getIntent().getStringExtra("full_name");
        if (userName == null || userName.isEmpty()) {
            SessionManager sessionManager = new SessionManager(this);
            if (sessionManager.isLoggedIn()) {
                userName = sessionManager.getUsername();
            }
        }
        String timeBasedGreeting = getTimeBasedGreeting();
        if (userName != null && !userName.isEmpty()) {
            txtGreeting.setText(String.format("%s, %s!", timeBasedGreeting, userName));
        } else {
            txtGreeting.setText(String.format("%s!", timeBasedGreeting));
        }
    }

    private String getTimeBasedGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 12) {
            return "¡Buenos días";
        } else if (hour >= 12 && hour < 18) {
            return "¡Buenas tardes";
        } else {
            return "¡Buenas noches";
        }
    }

    private void sendFinancialDataToWebhook() {
        try {
            String userId = String.valueOf(new SessionManager(this).getUserId());
            Map<String, Object> data = new HashMap<>();
            data.put("total_balance", currentBalance);
            data.put("monthly_income", txtIncome.getText().toString());
            data.put("monthly_expenses", txtExpenses.getText().toString());
            data.put("monthly_savings", txtSavings.getText().toString());
            webhookClient.sendFinancialData(userId, data, new N8nWebhookClient.WebhookCallback() {
                @Override
                public void onSuccess(N8nWebhookService.N8nResponse response) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "✅ Datos sincronizados con n8n", Toast.LENGTH_SHORT).show());
                }
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "⚠️ Error de sincronización: " + error, Toast.LENGTH_SHORT).show());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testWebhookConnection() {
        N8nWebhookClient.getInstance().testConnection(new N8nWebhookClient.WebhookCallback() {
            @Override
            public void onSuccess(N8nWebhookService.N8nResponse response) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Conexión exitosa con n8n", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Webhook test successful: " + response.getMessage());
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error al conectar con n8n: " + error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Webhook test failed: " + error);
                });
            }
        });
    }
    
    /**
     * Método para probar el webhook manualmente desde el botón de ajustes
     */
    private void probarWebhookManual() {
        Toast.makeText(this, "Conectando al webhook...", Toast.LENGTH_SHORT).show();
        
        WebhookHelper webhook = new WebhookHelper();
        
        webhook.sendGetRequest(new WebhookHelper.WebhookListener() {
            @Override
            public void onSuccess(String response, int code) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, 
                        "✅ Webhook conectado exitosamente\nCódigo: " + code, 
                        Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Webhook manual test - Success: " + response);
                    Log.d(TAG, "Response code: " + code);
                });
            }
            
            @Override
            public void onError(String error, int code) {
                runOnUiThread(() -> {
                    String mensaje = "❌ Error al conectar\n" + error;
                    if (code == 404) {
                        mensaje += "\n\n⚠️ Verifica que el workflow en n8n esté activo";
                    } else if (code == 0) {
                        mensaje += "\n\n⚠️ Verifica tu conexión a Internet";
                    }
                    
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Webhook manual test - Error: " + error + " (Code: " + code + ")");
                });
            }
        });
    }
}
