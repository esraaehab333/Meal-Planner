package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealplanner.R;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;
import com.facebook.FacebookRequestError;

public class CategoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        CategoryFragmentArgs args = CategoryFragmentArgs.fromBundle(getArguments());
        Category category = args.getCategory();
        Log.d("TAG",category.toString());
        return view;
    }
}