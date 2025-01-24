package com.example.versa.recyclerview;

public class RoomRecView {
    String roomName;
    static int roomId;

    public RoomRecView(String roomName, int roomId){
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    public int getRoomId() {
        return roomId;
    }
    public void setRoomId(int roomId) {
        RoomRecView.roomId = roomId;
    }
}
