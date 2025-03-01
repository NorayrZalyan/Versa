package com.example.versa.clients;

public class Client {

    private String name;
    private String phone;
    private String email;
    private String description;


    public Client(String name, String phone, String email, String description) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
