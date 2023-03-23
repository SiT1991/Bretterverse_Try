package com.example.bretterverse;

public class User {
    private String username;
    private String password;
    private String email;
    private String language;

    public User(String username, String password, String email, String language) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.language = language;
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
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}