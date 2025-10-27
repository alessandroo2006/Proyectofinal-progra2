package com.example.prueba;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String username;
    private String password;
    private String email;
    private String fullName;
    private long createdAt;
    private String dreams;
    private int daysSaved;

    // Constructor sin argumentos - Room lo usará
    public User() {}

    // Constructor con argumentos - ignorado por Room
    @Ignore
    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = System.currentTimeMillis();
        this.dreams = ""; // Inicializar dreams como string vacío
        this.daysSaved = 0; // Inicializar daysSaved como 0
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // Almacenar contraseña directamente (el hash se hará en el repositorio)
        this.password = password;
    }
    
    public void setPasswordHashed(String password) {
        // Hash de la contraseña antes de almacenarla
        this.password = SecurityHelper.hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getDreams() {
        return dreams;
    }

    public void setDreams(String dreams) {
        this.dreams = dreams;
    }

    public int getDaysSaved() {
        return daysSaved;
    }

    public void setDaysSaved(int daysSaved) {
        this.daysSaved = daysSaved;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
