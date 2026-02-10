package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.favorite.local.FavoriteLocalDataSource;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.presenter.HomePresenter;
import com.example.mealplanner.presentation.home.presenter.HomePresenterImp;
import com.example.mealplanner.utils.CustomSnackbar;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeFragment extends Fragment
        implements HomeView, OnMealClick, OnCategoryClick {

    private RecyclerView categoriesRecyclerView, popularRecyclerView;
    private ImageView mealOfDayImage;
    private TextView mealOfDayName, mealOfDayCategory, mealOfDayArea;
    private EditText searchEditText;
    private MaterialButton viewRecipeBtn;

    private CategoryListAdapter categoryAdapter;
    private PopularListAdapter popularAdapter;
    private HomePresenter presenter;

    private Meal mealOfTheDay;
    private FavoriteLocalDataSource favoriteDataSource;
    private SharedPreferanceLocalDataSource sharedPreferanceLocalDataSource;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        setupRecyclerViews();

        sharedPreferanceLocalDataSource = new SharedPreferanceLocalDataSource(requireContext());
        favoriteDataSource = new FavoriteLocalDataSource(requireContext(), sharedPreferanceLocalDataSource.getUserId());

        presenter = new HomePresenterImp(this, new MealRemoteDataSource(), favoriteDataSource, sharedPreferanceLocalDataSource);
        loadData();

        searchEditText.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_homeFragment_to_searchFragment));

        viewRecipeBtn.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                NavHostFragment.findNavController(this)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(mealOfTheDay));
            }
        });

        mealOfDayImage.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                NavHostFragment.findNavController(this)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(mealOfTheDay));
            }
        });
    }

    private void initViews(View view) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        popularRecyclerView = view.findViewById(R.id.popularRecyclerView);
        mealOfDayImage = view.findViewById(R.id.mealImage);
        mealOfDayName = view.findViewById(R.id.mealTitle);
        mealOfDayCategory = view.findViewById(R.id.mealCategory);
        mealOfDayArea = view.findViewById(R.id.mealArea);
        searchEditText = view.findViewById(R.id.searchEditText);
        viewRecipeBtn = view.findViewById(R.id.viewRecipeBtn);
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryListAdapter(this);
        categoriesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoryAdapter);

        popularAdapter = new PopularListAdapter(this);
        popularRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        popularRecyclerView.setAdapter(popularAdapter);
    }

    private void loadData() {
        presenter.getCategoryList();
        presenter.getPopularList();
        presenter.getMealOfDay();
    }
    @Override
    public void onGetCategorySuccess(List<Category> categories) {
        categoryAdapter.setCategoryList(categories);
    }

    @Override
    public void onGetPopularSuccess(List<Meal> meals) {
        popularAdapter.setMealList(meals);
        popularAdapter.setFavoriteIds(presenter.getFavoriteMealIds());
    }

    @Override
    public void onGetMealOfDaySuccess(List<Meal> meals) {
        mealOfTheDay = meals.get(0);
        mealOfDayName.setText(mealOfTheDay.getStrMeal());
        mealOfDayCategory.setText(mealOfTheDay.getStrCategory());
        mealOfDayArea.setText(mealOfTheDay.getStrArea());

        Glide.with(requireContext())
                .load(mealOfTheDay.getStrMealThumb())
                .placeholder(R.drawable.img_meal_test)
                .into(mealOfDayImage);
    }

    @Override
    public void onAddToFavorite(Meal meal) {
        presenter.addToFavorite(meal);
    }

    @Override
    public void onRemoveFromFavorite(Meal meal) {
        presenter.removeFromFavorite(meal);
    }

    @Override
    public void onMealClick(Meal meal) {
        NavHostFragment.findNavController(this)
                .navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(meal));
    }

    @Override
    public void OnCatecoryClick(Category category) {
        NavHostFragment.findNavController(this)
                .navigate(HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category));
        CustomSnackbar.showError(requireView(), category.getStrCategory());
    }

    @Override
    public void onFailure(String errorMessage) {
        CustomSnackbar.showError(getView(), errorMessage);
    }

    @Override
    public void onNoInternet() {
        CustomSnackbar.showError(getView(), "No internet connection");
    }
    @Override
    public void updateFavoriteIds(List<String> favoriteIds) {
        popularAdapter.setFavoriteIds(favoriteIds);
    }
}
