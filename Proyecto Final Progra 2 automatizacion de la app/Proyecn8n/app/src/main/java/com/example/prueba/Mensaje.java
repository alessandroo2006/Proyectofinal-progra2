package com.example.prueba;

public class Mensaje {
    private String contenido;
    private boolean esUsuario;

    public Mensaje(String contenido, boolean esUsuario) {
        this.contenido = contenido;
        this.esUsuario = esUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(boolean esUsuario) {
        this.esUsuario = esUsuario;
    }
}
