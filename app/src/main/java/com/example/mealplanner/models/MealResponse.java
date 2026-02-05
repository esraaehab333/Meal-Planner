package com.example.mealplanner.models;

import java.util.ArrayList;

public class MealResponse {
    ArrayList<Meal> meals = new ArrayList <Meal> ();

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}