package com.example.prueba;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private double amount;           // Monto del gasto
    private String merchant;         // Negocio/comercio
    private String category;         // Categoría
    private long date;              // Fecha del gasto (timestamp)
    private String inputMethod;     // Método de entrada: "voice", "manual", etc.
    private String originalText;    // Texto original (si fue por voz)
    private int userId;            // ID del usuario
    
    // Constructor vacío para Room
    public Expense() {}
    
    // Constructor completo
    public Expense(double amount, String merchant, String category, long date, String inputMethod, String originalText, int userId) {
        this.amount = amount;
        this.merchant = merchant;
        this.category = category;
        this.date = date;
        this.inputMethod = inputMethod;
        this.originalText = originalText;
        this.userId = userId;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getMerchant() {
        return merchant;
    }
    
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public long getDate() {
        return date;
    }
    
    public void setDate(long date) {
        this.date = date;
    }
    
    public String getInputMethod() {
        return inputMethod;
    }
    
    public void setInputMethod(String inputMethod) {
        this.inputMethod = inputMethod;
    }
    
    public String getOriginalText() {
        return originalText;
    }
    
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    // Helper methods
    public String getFormattedAmount() {
        return String.format("Q %.2f", amount);
    }
    
    public String getFormattedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(date));
    }
}

