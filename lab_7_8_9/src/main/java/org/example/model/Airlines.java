package org.example.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Airlines {
    private ArrayList<Airline> airlines;

    public Airlines() {
        airlines = new ArrayList<>();
    }
}
