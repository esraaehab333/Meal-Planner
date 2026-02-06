package com.example.mealplanner.models;

public class Ingredient {
    private String name;
    private String amount;
    private String image;

    public Ingredient(String name, String amount, String image) {
        this.name = name;
        this.amount = amount;
        this.image = image;
    }

    public String getName() { return name; }
    public String getAmount() { return amount; }
    public String getImage() { return image; }
}
