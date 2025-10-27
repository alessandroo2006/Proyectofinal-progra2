package com.example.prueba;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferencesManager {
    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_MONTHLY_SALARY = "monthly_salary";
    private static final String KEY_FIRST_TIME_USER = "first_time_user";
    private static final String KEY_CATEGORIES_CONFIGURED = "categories_configured";
    
    // Category keys
    private static final String KEY_ALIMENTOS = "category_alimentos";
    private static final String KEY_SUSCRIPCIONES = "category_suscripciones";
    private static final String KEY_JUEGOS = "category_juegos";
    private static final String KEY_FAMILIA = "category_familia";
    private static final String KEY_EMERGENCIAS = "category_emergencias";
    
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    
    public UserPreferencesManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    
    // Salary management
    public void setMonthlySalary(double salary) {
        editor.putFloat(KEY_MONTHLY_SALARY, (float) salary);
        editor.commit();
    }
    
    public double getMonthlySalary() {
        return pref.getFloat(KEY_MONTHLY_SALARY, 0.0f);
    }
    
    // First time user management
    public boolean isFirstTimeUser() {
        return pref.getBoolean(KEY_FIRST_TIME_USER, true);
    }
    
    public void setFirstTimeUser(boolean isFirstTime) {
        editor.putBoolean(KEY_FIRST_TIME_USER, isFirstTime);
        editor.commit();
    }
    
    // Categories configuration
    public boolean areCategoriesConfigured() {
        return pref.getBoolean(KEY_CATEGORIES_CONFIGURED, false);
    }
    
    public void setCategoriesConfigured(boolean configured) {
        editor.putBoolean(KEY_CATEGORIES_CONFIGURED, configured);
        editor.commit();
    }
    
    // Category budget management
    public void setCategoryBudget(String category, double budget) {
        switch (category) {
            case "alimentos":
                editor.putFloat(KEY_ALIMENTOS, (float) budget);
                break;
            case "suscripciones":
                editor.putFloat(KEY_SUSCRIPCIONES, (float) budget);
                break;
            case "juegos":
                editor.putFloat(KEY_JUEGOS, (float) budget);
                break;
            case "familia":
                editor.putFloat(KEY_FAMILIA, (float) budget);
                break;
            case "emergencias":
                editor.putFloat(KEY_EMERGENCIAS, (float) budget);
                break;
        }
        editor.commit();
    }
    
    public double getCategoryBudget(String category) {
        switch (category) {
            case "alimentos":
                return pref.getFloat(KEY_ALIMENTOS, 0.0f);
            case "suscripciones":
                return pref.getFloat(KEY_SUSCRIPCIONES, 0.0f);
            case "juegos":
                return pref.getFloat(KEY_JUEGOS, 0.0f);
            case "familia":
                return pref.getFloat(KEY_FAMILIA, 0.0f);
            case "emergencias":
                return pref.getFloat(KEY_EMERGENCIAS, 0.0f);
            default:
                return 0.0f;
        }
    }
    
    // Get all category names
    public String[] getCategoryNames() {
        return new String[]{"alimentos", "suscripciones", "juegos", "familia", "emergencias"};
    }
    
    // Get all category budgets
    public double[] getAllCategoryBudgets() {
        return new double[]{
            getCategoryBudget("alimentos"),
            getCategoryBudget("suscripciones"),
            getCategoryBudget("juegos"),
            getCategoryBudget("familia"),
            getCategoryBudget("emergencias")
        };
    }
}
