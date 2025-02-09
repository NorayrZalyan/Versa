package com.example.versa;
import java.util.ArrayList;
public class Category {
    String categoryName;
    String RoomId;
    ArrayList<Client> clients = new ArrayList<>();

    public Category(String categoryName, String RoomId){
        this.categoryName = categoryName;
        this.RoomId = RoomId;
    }

    public void addClient(Client client){
        clients.add(client);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getRoomId() {
        return RoomId;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }
}
