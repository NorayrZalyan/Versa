package com.example.versa;

public class Client {

    String name;
    String Description;

    Client(String name, String Description, String roomId, String category){
        this.name = name;
        this.Description = Description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return Description;
    }
}
