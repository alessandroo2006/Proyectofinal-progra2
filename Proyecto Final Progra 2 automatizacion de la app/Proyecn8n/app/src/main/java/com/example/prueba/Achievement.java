package com.example.prueba;

public class Achievement {
    private String name;
    private String description;
    private boolean unlocked;
    private String icon;
    
    public Achievement() {}
    
    public Achievement(String name, String description, boolean unlocked, String icon) {
        this.name = name;
        this.description = description;
        this.unlocked = unlocked;
        this.icon = icon;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
