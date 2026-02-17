package com.example.mealplanner.presentation.home.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.datasource.reposatory.MealRepositoryImpl;
import com.example.mealplanner.data.models.Category;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.presentation.home.presenter.HomePresenter;
import com.example.mealplanner.presentation.home.presenter.HomePresenterImp;
import com.example.mealplanner.utils.CustomDialog;
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
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private LinearLayout noInternetLayout;
    private MaterialButton retryButton;
    private CategoryListAdapter categoryAdapter;
    private PopularListAdapter popularAdapter;
    private HomePresenter presenter;
    private Meal mealOfTheDay;
    private AuthRepository authRepository;
    private MealRepository mealRepository;
    private int successCount = 0;
    private static final int TOTAL_REQUESTS = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerViews();
        setupListeners();

        authRepository = new AuthRepositoryImpl(requireActivity().getApplication());
        mealRepository = new MealRepositoryImpl(requireActivity().getApplication(),
                authRepository.getUserId());

        presenter = new HomePresenterImp(this,
                mealRepository,
                authRepository);

        loadData();
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
        scrollView = view.findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        noInternetLayout = view.findViewById(R.id.noInternetLayout);
        retryButton = view.findViewById(R.id.retryButton);
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

    private void setupListeners() {
        searchEditText.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_homeFragment_to_searchFragment));

        viewRecipeBtn.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                NavHostFragment.findNavController(this)
                        .navigate(HomeFragmentDirections
                                .actionHomeFragmentToMealFragment(null, mealOfTheDay.getIdMeal()));
            }
        });

        mealOfDayImage.setOnClickListener(v -> {
            if (mealOfTheDay != null) {
                NavHostFragment.findNavController(this)
                        .navigate(HomeFragmentDirections
                                .actionHomeFragmentToMealFragment(null, mealOfTheDay.getIdMeal()));
            }
        });

        retryButton.setOnClickListener(v -> loadData());
    }

    private void loadData() {
        if (!isNetworkAvailable()) {
            showNoInternet();
            return;
        }
        successCount = 0;
        showLoading();
        presenter.getCategoryList();
        presenter.getPopularList();
        presenter.getMealOfDay();
    }

    private void showLoading() {
        if (scrollView != null) scrollView.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
        if (noInternetLayout != null) noInternetLayout.setVisibility(View.GONE);
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
        if (noInternetLayout != null) noInternetLayout.setVisibility(View.GONE);
        if (scrollView != null) scrollView.setVisibility(View.VISIBLE);
    }

    private void showEmptyState() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (scrollView != null) scrollView.setVisibility(View.GONE);
        if (noInternetLayout != null) noInternetLayout.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.VISIBLE);
    }

    private void showNoInternet() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (scrollView != null) scrollView.setVisibility(View.GONE);
        if (emptyStateLayout != null) emptyStateLayout.setVisibility(View.GONE);
        if (noInternetLayout != null) noInternetLayout.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false;

                NetworkCapabilities capabilities =
                        connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                );
            } else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }

    private void checkAllDataLoaded() {
        successCount++;
        if (successCount >= TOTAL_REQUESTS) {
            successCount = 0;
            boolean hasCategories = categoryAdapter.getItemCount() > 0;
            boolean hasPopular = popularAdapter.getItemCount() > 0;
            boolean hasMealOfDay = mealOfTheDay != null;
            if (!hasCategories && !hasPopular && !hasMealOfDay) {
                showEmptyState();
            } else {
                showContent();
            }
        }
    }

    private boolean isGuestUser() {
        return "GUEST".equals(authRepository.getUserId());
    }

    private void showGuestLimitationDialog() {
        String message = "This feature is not available for <highlight>Guest</highlight> users. Please sign up to save favorites!";
        CustomDialog dialog = CustomDialog.newInstance(
                R.drawable.ic_lock,
                "Feature Locked",
                message,
                "Sign Up",
                "Cancel",
                (dialogInterface, which) -> {
                    navigateToSignUp();
                },
                null
        );

        dialog.show(getParentFragmentManager(), "GuestLimitationDialog");
    }
    private void navigateToSignUp() {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav, true)
                .build();
        Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_loginFregment, null, navOptions);
    }

    @Override
    public void onGetCategorySuccess(List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            categoryAdapter.setCategoryList(categories);
        }
        checkAllDataLoaded();
    }

    @Override
    public void onGetPopularSuccess(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            popularAdapter.setMealList(meals);
            popularAdapter.setFavoriteIds(presenter.getFavoriteMealIds());
        }
        checkAllDataLoaded();
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
        }
        checkAllDataLoaded();
    }

    @Override
    public void onFailure(String errorMessage) {
        successCount = 0;
        if (!isNetworkAvailable()) {
            showNoInternet();
        } else {
            showEmptyState();
            if (getView() != null) {
                CustomSnackbar.showError(getView(), errorMessage);
            }
        }
    }

    @Override
    public void onNoInternet() {
        successCount = 0;
        showNoInternet();
    }

    @Override
    public void updateFavoriteIds(List<String> favoriteIds) {
        if (popularAdapter != null) {
            popularAdapter.setFavoriteIds(favoriteIds);
        }
    }

    @Override
    public void onAddToFavorite(Meal meal) {
        if (isGuestUser()) {
            showGuestLimitationDialog();
            return;
        }
        if (presenter != null) {
            presenter.addToFavorite(meal);
        }
    }

    @Override
    public void onRemoveFromFavorite(Meal meal) {
        if (isGuestUser()) {
            showGuestLimitationDialog();
            return;
        }
        if (presenter != null) {
            presenter.removeFromFavorite(meal);
        }
    }

    @Override
    public void onMealClick(Meal meal) {
        NavHostFragment.findNavController(this)
                .navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(null, meal.getIdMeal()));
    }

    @Override
    public void OnCatecoryClick(Category category) {
        NavHostFragment.findNavController(this)
                .navigate(HomeFragmentDirections.actionHomeFragmentToCategoryFragment(category));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter = null;
    }
}
