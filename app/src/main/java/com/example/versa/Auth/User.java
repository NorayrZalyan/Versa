package com.example.versa.Auth;

import com.example.versa.staff.Worker;

import java.util.ArrayList;
import java.util.Map;


public class User {
    private ArrayList<Map<String, String>> categories = new ArrayList<>();
    private ArrayList<Worker> rooms = new ArrayList<>();
    private String userId;
    private String name;
    private String email;

    public User() {
    }

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
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
    public ArrayList<Worker> getRooms() {
        return rooms;
    }
    public ArrayList<Map<String, String>> getCategories() {
        return categories;
    }
}
