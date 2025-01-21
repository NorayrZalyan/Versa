package com.example.versa;

public class User {

    private String userId;
    private String name;
    private String email;
    private String jobtitle;

    public User() {
        // Для Firebase
    }

    public User(String userId, String name, String email, String jobtitle) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.jobtitle = jobtitle;
    }

    public String getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public String getJobtitle() {
        return jobtitle;
    }
}
