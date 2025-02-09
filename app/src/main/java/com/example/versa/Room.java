package com.example.versa;

import java.util.ArrayList;

public class Room {

    String roomName;
    int roomId;
    ArrayList<Client> clients = new ArrayList<>();

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
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void addClent(Client client){
        clients.add(client);
    }
}
