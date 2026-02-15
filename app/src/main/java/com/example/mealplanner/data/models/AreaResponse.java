package com.example.mealplanner.data.models;

import java.util.ArrayList;

public class AreaResponse {
    ArrayList<Area> meals = new ArrayList<Area>();
    public ArrayList<Area> getAreas() {
        return meals;
    }
    public void setAreas(ArrayList<Area> meals) {
        this.meals = meals;
    }
}
