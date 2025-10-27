package com.example.prueba;

public class DealAlert {
    private String keywords;
    private double maxPrice;
    private String sources;
    private boolean active;
    
    public DealAlert() {}
    
    public DealAlert(String keywords, double maxPrice, String sources, boolean active) {
        this.keywords = keywords;
        this.maxPrice = maxPrice;
        this.sources = sources;
        this.active = active;
    }
    
    // Getters and Setters
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    
    public double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(double maxPrice) { this.maxPrice = maxPrice; }
    
    public String getSources() { return sources; }
    public void setSources(String sources) { this.sources = sources; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public String getFormattedPrice() {
        return "Q " + String.format("%.2f", maxPrice);
    }
}
