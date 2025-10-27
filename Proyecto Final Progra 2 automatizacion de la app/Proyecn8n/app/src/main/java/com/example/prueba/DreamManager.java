package com.example.prueba;

import java.util.ArrayList;
import java.util.List;

public class DreamManager {
    private static DreamManager instance;
    private List<Dream> dreams;
    
    private DreamManager() {
        dreams = new ArrayList<>();
        initializeSampleDreams();
    }
    
    public static DreamManager getInstance() {
        if (instance == null) {
            instance = new DreamManager();
        }
        return instance;
    }
    
    private void initializeSampleDreams() {
        // Casa Nueva
        Dream casaNueva = new Dream("Casa Nueva", 50000.0, new java.util.Date(System.currentTimeMillis() + (4 * 30 * 24 * 60 * 60 * 1000L)), "Alta");
        casaNueva.setCurrentAmount(15000.0);
        casaNueva.setCategory("Vivienda");
        dreams.add(casaNueva);
        
        // Fondo de Emergencia
        Dream fondoEmergencia = new Dream("Fondo de Emergencia", 15000.0, new java.util.Date(System.currentTimeMillis() + (2 * 30 * 24 * 60 * 60 * 1000L)), "Alta");
        fondoEmergencia.setCurrentAmount(8500.0);
        fondoEmergencia.setCategory("Emergencia");
        dreams.add(fondoEmergencia);
        
        // Vacaciones Europa
        Dream vacaciones = new Dream("Vacaciones Europa", 5000.0, new java.util.Date(System.currentTimeMillis() + (6 * 30 * 24 * 60 * 60 * 1000L)), "Media");
        vacaciones.setCurrentAmount(2800.0);
        vacaciones.setCategory("Viajes");
        dreams.add(vacaciones);
    }
    
    public List<Dream> getDreams() {
        return dreams;
    }
    
    public void addDream(Dream dream) {
        dreams.add(dream);
    }
    
    public void removeDream(String dreamId) {
        dreams.removeIf(dream -> dream.getId().equals(dreamId));
    }
    
    public Dream getDreamById(String dreamId) {
        for (Dream dream : dreams) {
            if (dream.getId().equals(dreamId)) {
                return dream;
            }
        }
        return null;
    }
    
    public void updateDream(Dream updatedDream) {
        for (int i = 0; i < dreams.size(); i++) {
            if (dreams.get(i).getId().equals(updatedDream.getId())) {
                dreams.set(i, updatedDream);
                break;
            }
        }
    }
    
    public double getTotalSaved() {
        return dreams.stream().mapToDouble(Dream::getCurrentAmount).sum();
    }
    
    public double getTotalTarget() {
        return dreams.stream().mapToDouble(Dream::getTargetAmount).sum();
    }
    
    public double getTotalProgress() {
        double totalTarget = getTotalTarget();
        if (totalTarget == 0) return 0;
        return (getTotalSaved() / totalTarget) * 100;
    }
}
