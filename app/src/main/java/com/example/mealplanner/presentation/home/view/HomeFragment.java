package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mealplanner.R;
import com.example.mealplanner.models.Category;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView categoriesRecyclerView;
    private CategoryListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        adapter = new CategoryListAdapter();
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        categoriesRecyclerView.setAdapter(adapter);
        loadCategories();
        return view;
    }

    private void loadCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("1","Breakfast",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        categories.add(new Category("2","Lunch",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        categories.add(new Category("3","Dinner",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        categories.add(new Category("4","Dessert",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg")); categories.add(new Category("1","Breakfast",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        categories.add(new Category("2","Lunch",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        categories.add(new Category("3","Dinner",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        categories.add(new Category("4","Dessert",
                "https://media.self.com/photos/622912847b959736301bfb91/3:2/w_2118,h_1412,c_limit/GettyImages-1301412050.jpg"));
        adapter.setCategoryList(categories);
    }
}
