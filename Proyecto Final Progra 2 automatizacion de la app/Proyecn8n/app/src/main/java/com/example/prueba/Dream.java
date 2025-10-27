package com.example.prueba;

import java.util.Date;

public class Dream {
    private String id;
    private String name;
    private double targetAmount;
    private double currentAmount;
    private Date deadline;
    private String priority;
    private String category;
    private String description;
    private Date createdAt;
    
    public Dream() {
        this.createdAt = new Date();
        this.currentAmount = 0.0;
    }
    
    public Dream(String name, double targetAmount, Date deadline, String priority) {
        this();
        this.name = name;
        this.targetAmount = targetAmount;
        this.deadline = deadline;
        this.priority = priority;
        this.id = "dream_" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    
    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }
    
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    // Helper methods
    public double getProgressPercentage() {
        if (targetAmount == 0) return 0;
        return (currentAmount / targetAmount) * 100;
    }
    
    public double getRemainingAmount() {
        return targetAmount - currentAmount;
    }
    
    public boolean isCompleted() {
        return currentAmount >= targetAmount;
    }
    
    public long getDaysRemaining() {
        if (deadline == null) return 0;
        long diff = deadline.getTime() - System.currentTimeMillis();
        return diff / (24 * 60 * 60 * 1000);
    }
}
