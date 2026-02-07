package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceDao;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.models.FavoriteEntity;
import com.example.mealplanner.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements OnFavoriteClick {

    private RecyclerView rvFavorites;
    private FavoriteListAdapter adapter;
    private FavoriteLocalDataSource favoriteLocalDataSource;
    private SharedPreferanceDao sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        adapter = new FavoriteListAdapter(this);

        rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFavorites.setAdapter(adapter);

        sharedPref = new SharedPreferanceLocalDataSource(requireContext());
        String currentUserId = sharedPref.getUserId();
        if (currentUserId != null) {
            favoriteLocalDataSource = new FavoriteLocalDataSource(requireContext(), currentUserId);
            favoriteLocalDataSource.getFavoriteMeals()
                    .observe(getViewLifecycleOwner(), favoriteEntities -> {
                        adapter.setMealList(mapFavoritesToMeals(favoriteEntities));
                    });
        } else {
            //navigate to login screen
        }
        return view;
    }

    @Override
    public void onClick(Meal meal) {
        FavoritesFragmentDirections.ActionFavoritesFragmentToMealFragment action =
                FavoritesFragmentDirections.actionFavoritesFragmentToMealFragment(meal);
        NavHostFragment.findNavController(this).navigate(action);
    }

    private List<Meal> mapFavoritesToMeals(List<FavoriteEntity> entities) {
        List<Meal> meals = new ArrayList<>();

        for (FavoriteEntity entity : entities) {
            Meal meal = new Meal();
            meal.setIdMeal(entity.idMeal);
            meal.setStrMeal(entity.strMeal);
            meal.setStrMealThumb(entity.strMealThumb);
            meal.setStrCategory(entity.strCategory);
            meal.setStrArea(entity.strArea);
            meal.setStrTags(entity.strTags);
            meal.setStrYoutube(entity.strYoutube);
            meal.setStrInstructions(entity.strInstructions);
            meal.setIngredientsFromEntity(entity);
            meal.setMeasuresFromEntity(entity);

            meals.add(meal);
        }
        return meals;
    }
}
