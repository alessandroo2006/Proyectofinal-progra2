package com.example.prueba.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de datos para enviar eventos al webhook de n8n
 * Representa el objeto JSON que se enviará en la petición POST
 */
public class EventoN8n {
    
    @SerializedName("evento")
    private String evento;
    
    @SerializedName("timestamp")
    private long timestamp;
    
    @SerializedName("usuario_id")
    private String usuarioId;
    
    // Constructor vacío (requerido por Gson)
    public EventoN8n() {
    }
    
    // Constructor con parámetros
    public EventoN8n(String evento) {
        this.evento = evento;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Constructor completo
    public EventoN8n(String evento, String usuarioId) {
        this.evento = evento;
        this.timestamp = System.currentTimeMillis();
        this.usuarioId = usuarioId;
    }
    
    // Getters y Setters
    public String getEvento() {
        return evento;
    }
    
    public void setEvento(String evento) {
        this.evento = evento;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    @Override
    public String toString() {
        return "EventoN8n{" +
                "evento='" + evento + '\'' +
                ", timestamp=" + timestamp +
                ", usuarioId='" + usuarioId + '\'' +
                '}';
    }
}

