package com.example.mealplanner.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi{

    private MealService mealService;
    private Retrofit retrofit;
    public static String baseUrl ="https://www.themealdb.com/api/json/v1/1/";
    public NetworkApi(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public MealService getMealService(){
        if(mealService== null){
            // if it is null use the create from retrofit to create it
            mealService = retrofit.create(MealService.class);
        }
        return mealService;
    }

}
