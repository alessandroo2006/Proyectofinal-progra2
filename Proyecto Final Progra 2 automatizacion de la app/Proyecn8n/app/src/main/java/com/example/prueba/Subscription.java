package com.example.prueba;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subscriptions")
public class Subscription {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private double amount;
    private String currency;
    private String frequency; // MENSUAL, ANUAL, TRIMESTRAL
    private long nextRenewalDate; // Timestamp
    private int notificationDaysBefore;
    
    // Constructors
    public Subscription() {}
    
    @androidx.room.Ignore
    public Subscription(String name, double amount, String currency, String frequency, 
                       long nextRenewalDate, int notificationDaysBefore) {
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.frequency = frequency;
        this.nextRenewalDate = nextRenewalDate;
        this.notificationDaysBefore = notificationDaysBefore;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public long getNextRenewalDate() { return nextRenewalDate; }
    public void setNextRenewalDate(long nextRenewalDate) { this.nextRenewalDate = nextRenewalDate; }
    
    public int getNotificationDaysBefore() { return notificationDaysBefore; }
    public void setNotificationDaysBefore(int notificationDaysBefore) { this.notificationDaysBefore = notificationDaysBefore; }
    
    // Helper methods
    public String getFormattedAmount() {
        return currency + " " + String.format("%.2f", amount);
    }
    
    public String getFormattedRenewalDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(nextRenewalDate));
    }
    
    public long getDaysUntilRenewal() {
        long currentTime = System.currentTimeMillis();
        long diffInMillis = nextRenewalDate - currentTime;
        return diffInMillis / (24 * 60 * 60 * 1000);
    }
    
    public String getDaysUntilRenewalText() {
        long days = getDaysUntilRenewal();
        if (days < 0) {
            return "Vencida";
        } else if (days == 0) {
            return "Hoy";
        } else if (days == 1) {
            return "Mañana";
        } else {
            return "En " + days + " días";
        }
    }
}
