package com.example.versa.Auth;

import java.util.ArrayList;

public class User {
    private ArrayList<Integer> categories = new ArrayList<>();
    private ArrayList<Integer> rooms = new ArrayList<>();
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

    public ArrayList<Integer> getRooms() {
        return rooms;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }
}
