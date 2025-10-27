package com.example.prueba;

import java.util.ArrayList;
import java.util.List;

public class AnalysisDataManager {
    private static AnalysisDataManager instance;
    private List<CategoryData> categories;
    
    private AnalysisDataManager() {
        categories = new ArrayList<>();
        initializeDefaultData();
    }
    
    public static AnalysisDataManager getInstance() {
        if (instance == null) {
            instance = new AnalysisDataManager();
        }
        return instance;
    }
    
    private void initializeDefaultData() {
        // Default data matching the image
        categories.add(new CategoryData("Alimentación", 1200.00, 35, 0xFF3498DB)); // Blue
        categories.add(new CategoryData("Transporte", 800.00, 23, 0xFF27AE60)); // Green
        categories.add(new CategoryData("Entretenimiento", 600.00, 17, 0xFFF39C12)); // Orange
        categories.add(new CategoryData("Servicios", 500.00, 15, 0xFF9B59B6)); // Purple
        categories.add(new CategoryData("Otros", 350.00, 10, 0xFF7F8C8D)); // Gray
    }
    
    public List<CategoryData> getCategories() {
        return categories;
    }
    
    public void updateCategoryAmount(int index, double newAmount) {
        if (index >= 0 && index < categories.size()) {
            CategoryData category = categories.get(index);
            category.setAmount(newAmount);
            recalculatePercentages();
        }
    }
    
    public void updateCategoryAmount(String categoryName, double newAmount) {
        for (CategoryData category : categories) {
            if (category.getName().equals(categoryName)) {
                category.setAmount(newAmount);
                recalculatePercentages();
                break;
            }
        }
    }
    
    private void recalculatePercentages() {
        double total = getTotalAmount();
        if (total > 0) {
            for (CategoryData category : categories) {
                double percentage = (category.getAmount() / total) * 100;
                category.setPercentage((int) Math.round(percentage));
            }
        }
    }
    
    public double getTotalAmount() {
        double total = 0;
        for (CategoryData category : categories) {
            total += category.getAmount();
        }
        return total;
    }
    
    public float[] getPercentages() {
        float[] percentages = new float[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            percentages[i] = (float) categories.get(i).getPercentage();
        }
        return percentages;
    }
    
    public int[] getColors() {
        int[] colors = new int[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            colors[i] = categories.get(i).getColor();
        }
        return colors;
    }
    
    public static class CategoryData {
        private String name;
        private double amount;
        private int percentage;
        private int color;
        
        public CategoryData(String name, double amount, int percentage, int color) {
            this.name = name;
            this.amount = amount;
            this.percentage = percentage;
            this.color = color;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        
        public int getPercentage() { return percentage; }
        public void setPercentage(int percentage) { this.percentage = percentage; }
        
        public int getColor() { return color; }
        public void setColor(int color) { this.color = color; }
    }
}
