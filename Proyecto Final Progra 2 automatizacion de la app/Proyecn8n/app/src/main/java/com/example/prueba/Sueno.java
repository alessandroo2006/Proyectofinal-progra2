package com.example.prueba;

public class Sueno {
    private String nombre;
    private double montoObjetivo;
    private double ahorroActual;
    private String fechaLimite;
    private String prioridad;

    public Sueno(String nombre, double montoObjetivo, double ahorroActual, String fechaLimite, String prioridad) {
        this.nombre = nombre;
        this.montoObjetivo = montoObjetivo;
        this.ahorroActual = ahorroActual;
        this.fechaLimite = fechaLimite;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(double montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }

    public double getAhorroActual() {
        return ahorroActual;
    }

    public void setAhorroActual(double ahorroActual) {
        this.ahorroActual = ahorroActual;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
