package com.example.mealplanner.data.network;

import com.example.mealplanner.data.models.AreaResponse;
import com.example.mealplanner.data.models.CategoryResponse;
import com.example.mealplanner.data.models.IngredientsResponse;
import com.example.mealplanner.data.models.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {

    // get popular meals
    @GET("search.php")
    Single<MealResponse> getPopularMeals(@Query("s") String empty);

    // get meal by name
    @GET("search.php")
    Single<MealResponse> getMealByName(@Query("s") String mealName);

    // get all meals by first letter
    @GET("search.php")
    Single<MealResponse> getMealByFirstLetter(@Query("f") String firstLetter);

    // get full meal details by id
    @GET("lookup.php")
    Single<MealResponse> getMealById(@Query("i") String mealId);

    // get a single random meal
    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    // get all meal categories
    @GET("categories.php")
    Single<CategoryResponse> getAllCategories();

    // get all Areas
    @GET("list.php?a=list")
    Single<AreaResponse> getAreasList();

    // get all Ingredients
    @GET("list.php?i=list")
    Single<IngredientsResponse> getIngredientsList();

    // filter by main ingredient
    @GET("filter.php")
    Single<MealResponse> filterByIngredient(@Query("i") String ingredient);

    // filter by Category
    @GET("filter.php")
    Single<MealResponse> filterByCategory(@Query("c") String category);

    // filter by Area
    @GET("filter.php")
    Single<MealResponse> filterByArea(@Query("a") String area);
}
