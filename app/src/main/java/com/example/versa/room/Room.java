package com.example.versa.room;

import com.example.versa.category.Category;

import java.util.ArrayList;
import java.util.Map;

public class Room {

    String roomName;
    int roomId;
    ArrayList<Map<String, String>> categories = new ArrayList<>();

    public Room(String roomName, int roomId){
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
    public String getRoomName() {
        return roomName;
    }
    public ArrayList<Map<String, String>> getCategories() {
        return categories;
    }
}
