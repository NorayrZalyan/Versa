package com.example.versa.Auth;

import java.util.ArrayList;

public class User {
    private ArrayList<Integer> categories = new ArrayList<>();
    private String userId;
    private String name;
    private String email;
    private String jobtitle;
    private String RoomId;

    public User() {
        // Для Firebase
    }

    public User(String userId, String name, String email, String jobtitle, String Roomid) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.jobtitle = jobtitle;
        this.RoomId = Roomid;
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
    public String getRoomId() {
        return RoomId;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }
}
