package com.example.mealplanner.presentation.home.presenter;

import com.example.mealplanner.datasource.meal.remote.MealNetworkResponse;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.view.HomeView;

import java.util.List;

public class HomePresenterImp implements HomePresenter {

    private HomeView view;
    private MealRemoteDataSource remoteDataSource;

    public HomePresenterImp(HomeView view, MealRemoteDataSource remoteDataSource) {
        this.view = view;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void getCategoryList() {
        remoteDataSource.getCategoryList(new MealNetworkResponse<Category>() {
            @Override
            public void onSuccess(List<Category> dataList) {
                if (view != null) {
                    view.onGetCategorySuccess((List) dataList);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    view.onFailure(errorMessage);
                }
            }

            @Override
            public void noInternet() {
                if (view != null) {
                    view.onNoInternet();
                }
            }
        });
    }

    @Override
    public void getPopularList() {
        remoteDataSource.getPopularList(new MealNetworkResponse<Meal>() {
            @Override
            public void onSuccess(List<Meal> dataList) {
                if (view != null) {
                    view.onGetPopularSuccess((List) dataList);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    view.onFailure(errorMessage);
                }
            }

            @Override
            public void noInternet() {
                if (view != null) {
                    view.onNoInternet();
                }
            }
        });
    }

    @Override
    public void getMealOfDay() {
        remoteDataSource.getMealOfDay(new MealNetworkResponse<Meal>() {
            @Override
            public void onSuccess(List<Meal> dataList) {
                if (view != null) {
                    view.onGetMealOfDaySuccess((List) dataList);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (view != null) {
                    view.onFailure(errorMessage);
                }
            }

            @Override
            public void noInternet() {
                if (view != null) {
                    view.onNoInternet();
                }
            }
        });
    }

}