package com.example.versa;

public class Client {

    String name;
    String Description;
    String roomId;
    String category;

    Client(String name, String Description, String roomId, String category){
        this.name = name;
        this.Description = Description;
        this.roomId = roomId;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getCategory() {
        return category;
    }
}
