package com.example.mealplanner.presentation.home.presenter;

import android.content.Context;

import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.models.FavoriteEntity;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.view.FavoriteMapper;
import com.example.mealplanner.presentation.home.view.MealView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MealPresenterImp implements MealPresenter{
    private MealView view;
    private FavoriteLocalDataSource localDataSource;
    private String currentUserId;
    public MealPresenterImp(MealView view, Context context) {
        this.view = view;
        this.currentUserId = getCurrentUserId();
        this.localDataSource = new FavoriteLocalDataSource(context, currentUserId);
    }
    @Override
    public void loadMeal(Meal meal) {
        if (view != null) {
            view.showMeal(meal);
        }
        isFavorite(meal.getIdMeal());
    }

    @Override
    public void addToFav(Meal meal) {
        FavoriteEntity entity =
                FavoriteMapper.fromMeal(meal, currentUserId);

        localDataSource.insertFavoriteMeal(entity);
        view.updateFavoriteStatus(true);
        view.showSuccessMessage("Added to favorites");
    }

    @Override
    public void deleteFromFav(Meal meal) {
        FavoriteEntity entity =
                FavoriteMapper.fromMeal(meal, currentUserId);

        localDataSource.deleteFavoriteMeal(entity);
        view.updateFavoriteStatus(false);
        view.showSuccessMessage("Removed from favorites");
    }


    @Override
    public void isFavorite(String mealId) {
        localDataSource.isFavorite(
                    mealId,
                    isFav -> view.updateFavoriteStatus(isFav)
            );
    }
    @Override
    public void onDestroy() {
        view = null;
    }
    private FavoriteEntity convertMealToFavoriteEntity(Meal meal) {
        List<String> ingredientsList = meal.getIngredientsList();
        List<String> measuresList = meal.getMeasuresList();
        String[] ingredients = ingredientsList.toArray(new String[0]);
        String[] measures = measuresList.toArray(new String[0]);
        return new FavoriteEntity(
                meal.getIdMeal(),
                currentUserId,
                meal.getStrMeal(),
                meal.getStrMealThumb(),
                meal.getStrCategory(),
                meal.getStrArea(),
                meal.getStrTags(),
                meal.getStrYoutube(),
                meal.getStrInstructions(),
                ingredients,
                measures
        );
    }

    private String getCurrentUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return "GUST";
    }
}
