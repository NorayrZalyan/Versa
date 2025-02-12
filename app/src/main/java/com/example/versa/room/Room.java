package com.example.versa.room;

import com.example.versa.category.Category;

import java.util.ArrayList;

public class Room {

    String roomName;
    int roomId;
    ArrayList<Category> clients = new ArrayList<>();

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

    public ArrayList<Category> getClients() {
        return clients;
    }
}
