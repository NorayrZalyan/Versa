package com.example.versa.staff;

public class Worker {
    String roomId;
    String roomName;
    String jobTitle;

    public Worker(String roomId, String roomName, String jobTitle) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.jobTitle = jobTitle;
    }

    public String getRoomId() {
        return roomId;
    }
    public String getRoomName() {
        return roomName;
    }
    public String getJobTitle() {
        return jobTitle;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public void setRoomName(String roomName) {
        this.roomName = Worker.this.roomName;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
