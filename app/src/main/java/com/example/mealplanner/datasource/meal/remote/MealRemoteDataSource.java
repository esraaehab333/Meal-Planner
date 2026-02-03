package com.example.mealplanner.datasource.meal.remote;

import com.example.mealplanner.data.network.MealService;
import com.example.mealplanner.data.network.NetworkApi;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.CategoryResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealRemoteDataSource {

    private MealService mealService;
    public MealRemoteDataSource(){

        this.mealService= new NetworkApi().getMealService();
    }
    public void getCategoryList(MealNetworkResponse mealCallBack){
        mealService.getAllCategories().enqueue(
                new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                        if(response.code() == 200){
                            CategoryResponse categoryResponse = response.body();
                            List<Category> categoryList = categoryResponse.getCategories();
                            // this is the call back to separation
                            mealCallBack.onSuccess(categoryList);
                        }
                        else {
                            mealCallBack.onFailure("Error server error");
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                        if(t instanceof IOException){
                            mealCallBack.noInternet();
                        }
                        else{
                            mealCallBack.onFailure("Convertion error!");
                        }
                    }
                }
        );
    }
}
