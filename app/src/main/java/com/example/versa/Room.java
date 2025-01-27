package com.example.versa;

public class Room {

    private String name;
    private static int id = 0;

    public Room(){
        id++;
    }
    public Room(String name){
        this.name = name;
        Room.id = ++Room.id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setId(int id) {
        Room.id = id;
    }
}
