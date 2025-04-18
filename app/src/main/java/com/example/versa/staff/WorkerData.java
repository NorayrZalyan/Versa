package com.example.versa.staff;

public class WorkerData {


    String name;
    String jobTitle;

    public WorkerData(String name, String jobTitle) {
        this.name = name;
        this.jobTitle = jobTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
