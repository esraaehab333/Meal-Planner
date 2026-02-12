package com.example.mealplanner.data.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi{

    private MealService mealService;
    private Retrofit retrofit;
    public static String baseUrl ="https://www.themealdb.com/api/json/v1/1/";
    public NetworkApi(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
    public MealService getMealService(){
        if(mealService== null){
            mealService = retrofit.create(MealService.class);
        }
        return mealService;
    }

}
