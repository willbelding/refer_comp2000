package com.example.wb_tennis_app.models;

import java.util.List;

public class Court {
    private String type;
    private String number;

    // Constructor with parameters
    public Court(String type, String number) {
        this.type = type;
        this.number = number;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    // Setters can be added here if needed
}
