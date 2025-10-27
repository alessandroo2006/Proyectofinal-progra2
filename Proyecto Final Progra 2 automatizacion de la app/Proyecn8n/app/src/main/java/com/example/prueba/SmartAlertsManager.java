package com.example.prueba;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Locale;

public class SmartAlertsManager {
    
    private UserPreferencesManager preferencesManager;
    private Context context;
    
    // Alert types
    public static final int ALERT_TYPE_BUDGET_WARNING = 1;
    public static final int ALERT_TYPE_SPENDING_INCREASE = 2;
    public static final int ALERT_TYPE_SAVINGS_ACHIEVEMENT = 3;
    public static final int ALERT_TYPE_UNDERSPENDING = 4;
    
    // Alert severity levels
    public static final int SEVERITY_LOW = 1;
    public static final int SEVERITY_MEDIUM = 2;
    public static final int SEVERITY_HIGH = 3;
    
    public SmartAlertsManager(Context context) {
        this.context = context;
        this.preferencesManager = new UserPreferencesManager(context);
    }
    
    /**
     * Generate smart alerts based on user's financial data
     */
    public List<SmartAlert> generateSmartAlerts() {
        List<SmartAlert> alerts = new ArrayList<>();
        
        try {
            // Get user data
            double monthlySalary = preferencesManager.getMonthlySalary();
            double[] categoryBudgets = preferencesManager.getAllCategoryBudgets();
            String[] categoryNames = preferencesManager.getCategoryNames();
            
            if (monthlySalary <= 0) {
                return alerts; // No alerts if no salary data
            }
            
            // Calculate total budget allocated
            double totalBudgetAllocated = 0;
            for (double budget : categoryBudgets) {
                totalBudgetAllocated += budget;
            }
            
            // Generate budget warning alerts
            alerts.addAll(generateBudgetWarningAlerts(categoryBudgets, categoryNames));
            
            // Generate spending increase alerts (simulated based on budget vs salary)
            alerts.addAll(generateSpendingIncreaseAlerts(categoryBudgets, categoryNames, monthlySalary));
            
            // Generate savings achievement alerts
            alerts.addAll(generateSavingsAchievementAlerts(totalBudgetAllocated, monthlySalary));
            
            // Generate underspending alerts
            alerts.addAll(generateUnderspendingAlerts(categoryBudgets, categoryNames));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return alerts;
    }
    
    /**
     * Generate budget warning alerts when spending is high
     */
    private List<SmartAlert> generateBudgetWarningAlerts(double[] categoryBudgets, String[] categoryNames) {
        List<SmartAlert> alerts = new ArrayList<>();
        
        // Simulate spending as a percentage of budget (random for demo)
        double[] simulatedSpending = {
            categoryBudgets[0] * 0.85, // 85% of food budget spent
            categoryBudgets[1] * 0.45, // 45% of subscriptions spent
            categoryBudgets[2] * 0.30, // 30% of games spent
            categoryBudgets[3] * 0.60, // 60% of family spent
            categoryBudgets[4] * 0.20  // 20% of emergencies spent
        };
        
        for (int i = 0; i < categoryBudgets.length; i++) {
            if (categoryBudgets[i] > 0 && simulatedSpending[i] > 0) {
                double percentageSpent = (simulatedSpending[i] / categoryBudgets[i]) * 100;
                
                if (percentageSpent >= 80) {
                    String categoryName = getCategoryDisplayName(categoryNames[i]);
                    String message = String.format(Locale.getDefault(), 
                        "Has gastado %.0f%% de tu presupuesto en %s", 
                        percentageSpent, categoryName.toLowerCase());
                    
                    int severity = percentageSpent >= 95 ? SEVERITY_HIGH : 
                                  percentageSpent >= 85 ? SEVERITY_MEDIUM : SEVERITY_LOW;
                    
                    int color = getBudgetWarningColor(percentageSpent);
                    
                    alerts.add(new SmartAlert(
                        ALERT_TYPE_BUDGET_WARNING,
                        message,
                        categoryName,
                        severity,
                        color
                    ));
                }
            }
        }
        
        return alerts;
    }
    
    /**
     * Generate spending increase alerts
     */
    private List<SmartAlert> generateSpendingIncreaseAlerts(double[] categoryBudgets, String[] categoryNames, double monthlySalary) {
        List<SmartAlert> alerts = new ArrayList<>();
        
        // Simulate spending increases (random for demo)
        double[] spendingIncreases = {0.15, 0.40, 0.25, 0.10, 0.05}; // 15%, 40%, 25%, 10%, 5%
        
        for (int i = 0; i < categoryBudgets.length; i++) {
            if (categoryBudgets[i] > 0 && spendingIncreases[i] > 0.20) { // Alert if increase > 20%
                String categoryName = getCategoryDisplayName(categoryNames[i]);
                double increasePercentage = spendingIncreases[i] * 100;
                
                String message = String.format(Locale.getDefault(),
                    "Gastos en %s han aumentado %.0f%% este mes",
                    categoryName.toLowerCase(), increasePercentage);
                
                int severity = increasePercentage >= 50 ? SEVERITY_HIGH : SEVERITY_MEDIUM;
                int color = getSpendingIncreaseColor(increasePercentage);
                
                alerts.add(new SmartAlert(
                    ALERT_TYPE_SPENDING_INCREASE,
                    message,
                    categoryName,
                    severity,
                    color
                ));
            }
        }
        
        return alerts;
    }
    
    /**
     * Generate savings achievement alerts
     */
    private List<SmartAlert> generateSavingsAchievementAlerts(double totalBudgetAllocated, double monthlySalary) {
        List<SmartAlert> alerts = new ArrayList<>();
        
        double savings = monthlySalary - totalBudgetAllocated;
        double savingsPercentage = (savings / monthlySalary) * 100;
        
        // Simulate previous month savings for comparison
        double previousMonthSavings = savings * 0.7; // Assume 30% less savings last month
        double savingsIncrease = savings - previousMonthSavings;
        
        if (savingsIncrease > 0 && savingsIncrease >= 500) {
            String message = String.format(Locale.getDefault(),
                "¡Buen trabajo! Ahorraste Q%.0f más que el mes pasado",
                savingsIncrease);
            
            alerts.add(new SmartAlert(
                ALERT_TYPE_SAVINGS_ACHIEVEMENT,
                message,
                "Ahorro",
                SEVERITY_LOW, // Positive alert
                0xFF3498DB // Blue color for positive
            ));
        }
        
        return alerts;
    }
    
    /**
     * Generate underspending alerts
     */
    private List<SmartAlert> generateUnderspendingAlerts(double[] categoryBudgets, String[] categoryNames) {
        List<SmartAlert> alerts = new ArrayList<>();
        
        // Check if user has allocated very little to certain categories
        double totalBudget = 0;
        for (double budget : categoryBudgets) {
            totalBudget += budget;
        }
        
        if (totalBudget > 0) {
            for (int i = 0; i < categoryBudgets.length; i++) {
                if (categoryBudgets[i] > 0) {
                    double percentageOfTotal = (categoryBudgets[i] / totalBudget) * 100;
                    
                    // Alert if category gets less than 5% of total budget
                    if (percentageOfTotal < 5 && categoryBudgets[i] < 500) {
                        String categoryName = getCategoryDisplayName(categoryNames[i]);
                        String message = String.format(Locale.getDefault(),
                            "Considera aumentar tu presupuesto para %s",
                            categoryName.toLowerCase());
                        
                        alerts.add(new SmartAlert(
                            ALERT_TYPE_UNDERSPENDING,
                            message,
                            categoryName,
                            SEVERITY_LOW,
                            0xFF95A5A6 // Gray color
                        ));
                    }
                }
            }
        }
        
        return alerts;
    }
    
    /**
     * Get display name for category
     */
    private String getCategoryDisplayName(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "alimentos": return "Alimentación";
            case "suscripciones": return "Suscripciones";
            case "juegos": return "Entretenimiento";
            case "familia": return "Familia";
            case "emergencias": return "Emergencias";
            default: return capitalizeFirstLetter(categoryName);
        }
    }
    
    /**
     * Capitalize first letter of string
     */
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
    
    /**
     * Get color for budget warning based on percentage
     */
    private int getBudgetWarningColor(double percentage) {
        if (percentage >= 95) return 0xFFE74C3C; // Red
        if (percentage >= 85) return 0xFFF39C12; // Orange
        return 0xFFF1C40F; // Yellow
    }
    
    /**
     * Get color for spending increase based on percentage
     */
    private int getSpendingIncreaseColor(double percentage) {
        if (percentage >= 50) return 0xFFE74C3C; // Red
        if (percentage >= 30) return 0xFFF39C12; // Orange
        return 0xFFE67E22; // Dark orange
    }
    
    /**
     * Smart Alert data class
     */
    public static class SmartAlert {
        public int type;
        public String message;
        public String category;
        public int severity;
        public int color;
        
        public SmartAlert(int type, String message, String category, int severity, int color) {
            this.type = type;
            this.message = message;
            this.category = category;
            this.severity = severity;
            this.color = color;
        }
    }
}
