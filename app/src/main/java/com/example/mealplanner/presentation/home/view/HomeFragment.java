package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.utils.CustomSnackbar;
import com.example.mealplanner.datasource.meal.remote.MealRemoteDataSource;
import com.example.mealplanner.models.Category;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.presenter.HomePresenter;
import com.example.mealplanner.presentation.home.presenter.HomePresenterImp;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeFragment extends Fragment implements HomeView, OnMealClick,OnCatecoryClick {

    private RecyclerView categoriesRecyclerView;
    private RecyclerView popularRecyclerView;
    private ImageView mealOfDayImage;
    private TextView mealOfDayName;
    private TextView mealOfDayCategory;
    private TextView mealOfDayArea;
    private EditText searchEditText;
    private CategoryListAdapter categoryAdapter;
    private PopularListAdapter popularAdapter;
    private HomePresenter presenter;
    private MaterialButton viewRecipeBtn;
    private Meal mealOfTheDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerViews();
        initPresenter();
        loadData();
        searchEditText.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_homeFragment_to_searchFragment);
        });
        viewRecipeBtn.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                HomeFragmentDirections.ActionHomeFragmentToMealFragment action =
                        HomeFragmentDirections.actionHomeFragmentToMealFragment(mealOfTheDay);
                NavHostFragment.findNavController(this).navigate(action);
            } else {
             CustomSnackbar.showError(view,"Meal not loaded yet");
            }
        });
        mealOfDayImage.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                HomeFragmentDirections.ActionHomeFragmentToMealFragment action =
                        HomeFragmentDirections.actionHomeFragmentToMealFragment(mealOfTheDay);
                NavHostFragment.findNavController(this).navigate(action);
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
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        categoriesRecyclerView.setAdapter(categoryAdapter);
        categoriesRecyclerView.setHasFixedSize(true);

        popularAdapter = new PopularListAdapter(this);
        popularRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );
        popularRecyclerView.setAdapter(popularAdapter);
        popularRecyclerView.setHasFixedSize(false);
    }
    private void initPresenter() {
        MealRemoteDataSource remoteDataSource = new MealRemoteDataSource();
        presenter = new HomePresenterImp(this, remoteDataSource);
    }
    private void loadData() {
        presenter.getCategoryList();
        presenter.getPopularList();
        presenter.getMealOfDay();
    }
    @Override
    public void onGetCategorySuccess(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            categoryAdapter.setCategoryList(categories);
        }
    }

    @Override
    public void onGetPopularSuccess(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            popularAdapter.setMealList(meals);
        } else {
            CustomSnackbar.showError(getView(),"No popular meals found");
        }
    }

    @Override
    public void onGetMealOfDaySuccess(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            mealOfTheDay = meals.get(0);
            mealOfDayName.setText(mealOfTheDay.getStrMeal());
            mealOfDayCategory.setText(mealOfTheDay.getStrCategory());
            mealOfDayArea.setText(mealOfTheDay.getStrArea());
            Glide.with(requireContext())
                    .load(mealOfTheDay.getStrMealThumb())
                    .placeholder(R.drawable.img_meal_test)
                    .into(mealOfDayImage);
        } else {
            CustomSnackbar.showError(getView(), "No meal of the day found");
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        CustomSnackbar.showError(getView(), errorMessage);
    }

    @Override
    public void onNoInternet() {
        CustomSnackbar.showError(getView(),"No internet connection");
    }
    @Override
    public void onMealClick(Meal meal) {
        HomeFragmentDirections.ActionHomeFragmentToMealFragment action =
                HomeFragmentDirections.actionHomeFragmentToMealFragment(meal);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void OnCatecoryClick(Category category) {
        HomeFragmentDirections.ActionHomeFragmentToCategoryFragment action =
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category);
        NavHostFragment.findNavController(this).navigate(action);
        Toast.makeText(getContext(),
                "Clicked: " + category.getStrCategory(),
                Toast.LENGTH_SHORT).show();
    }
}
