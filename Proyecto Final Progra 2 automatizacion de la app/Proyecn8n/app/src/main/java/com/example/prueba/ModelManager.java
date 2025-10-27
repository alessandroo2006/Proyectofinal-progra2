package com.example.prueba;

import java.util.Arrays;
import java.util.List;

public class ModelManager {
    // Lista de modelos a probar en orden de preferencia
    private static final List<String> AVAILABLE_MODELS = Arrays.asList(
        "llama-3.3-70b-versatile",
        "llama-3.1-70b-versatile", 
        "llama-3.1-8b-instant",
        "mixtral-8x7b-32768",
        "gemma-7b-it",
        "llama3-8b-8192"
    );
    
    private static int currentModelIndex = 0;
    
    public static String getCurrentModel() {
        return AVAILABLE_MODELS.get(currentModelIndex);
    }
    
    public static String getNextModel() {
        currentModelIndex = (currentModelIndex + 1) % AVAILABLE_MODELS.size();
        return AVAILABLE_MODELS.get(currentModelIndex);
    }
    
    public static List<String> getAllModels() {
        return AVAILABLE_MODELS;
    }
    
    public static void resetToFirstModel() {
        currentModelIndex = 0;
    }
}
