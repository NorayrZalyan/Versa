package com.example.versa.category;

import com.example.versa.classes.Client;

import java.util.ArrayList;

public class Category {

    private String name;

    private ArrayList<Client> clents = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Client> getClents() {
        return clents;
    }


    public void addClient(Client client) {
        clents.add(client);
    }
}
