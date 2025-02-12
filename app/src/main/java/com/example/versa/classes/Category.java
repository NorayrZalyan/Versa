package com.example.versa.classes;

import java.util.ArrayList;

public class Category {

    private String name;

    private ArrayList<Clent> clents = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Clent> getClents() {
        return clents;
    }
}
