package com.example.mealplanner.presentation.search.presenter;

public interface SearchPresenter {
    void searchByIngredient(String ingredient);
    void searchByCategory(String category);
    void searchByArea(String area);
    void searchByName(String name);
    void getListArea();
    void getListIngredients();
    void getListCategory();
}