package com.example.versa;

public class Room {

    private String name;
    private static int nextid = 0;
    private int id;

    public Room(){

    }
    public Room(String name){
        this.name = name;
        this.id = ++nextid;
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

    public void setId(int id) {
        this.id = id;
    }
}
