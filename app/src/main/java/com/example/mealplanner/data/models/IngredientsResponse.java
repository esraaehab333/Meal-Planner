package com.example.mealplanner.data.models;

import java.util.ArrayList;

public class IngredientsResponse {
    ArrayList<Ingredient> meals = new ArrayList <Ingredient> ();
    public ArrayList<Ingredient> getIngradiants() {
        return meals;
    }

    public void setIngradiants(ArrayList<Ingredient> meals) {
        this.meals = meals;
    }
}