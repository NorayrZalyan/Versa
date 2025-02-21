package com.example.versa.category;

import com.example.versa.classes.Client;

import java.util.ArrayList;

public class Category {

    private String name;

    private ArrayList<Client> clients = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }


    public void addClient(Client client) {
        clients.add(client);
    }
}
