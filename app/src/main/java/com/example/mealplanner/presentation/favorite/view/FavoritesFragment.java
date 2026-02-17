package com.example.mealplanner.presentation.favorite.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.datasource.reposatory.MealRepositoryImpl;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.presentation.favorite.presenter.FavoritePresenter;
import com.example.mealplanner.presentation.favorite.presenter.FavoritePresenterImp;
import com.example.mealplanner.utils.mapper.FavoriteMapper;
import com.example.mealplanner.presentation.home.view.OnFavoriteClick;
import com.example.mealplanner.utils.CustomDialog;
import com.example.mealplanner.utils.CustomSnackbar;

import java.util.List;

public class FavoritesFragment extends Fragment implements OnFavoriteClick, FavoriteView {

    private RecyclerView rvFavorites;
    private LinearLayout llEmptyState;
    private FavoriteListAdapter adapter;
    private FavoritePresenter presenter;
    private AuthRepository authRepository;
    private MealRepository mealRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        initViews(view);
        setupRecyclerView();
        checkUserAndLoadFavorites();

        return view;
    }

    private void initViews(View view) {
        rvFavorites = view.findViewById(R.id.rvFavorites);
        llEmptyState = view.findViewById(R.id.llEmptyState);
    }

    private void setupRecyclerView() {
        adapter = new FavoriteListAdapter(this);
        rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFavorites.setAdapter(adapter);
    }

    private void checkUserAndLoadFavorites() {
        authRepository = new AuthRepositoryImpl(requireActivity().getApplication());
        String currentUserId = authRepository.getUserId();

        if ("GUEST".equals(currentUserId)) {
            showEmptyState();
            showGuestLimitationDialog();
        } else {
            if (currentUserId != null) {
                mealRepository = new MealRepositoryImpl(requireActivity().getApplication(), currentUserId);
                presenter = new FavoritePresenterImp(this, mealRepository);
                presenter.getFavoriteMeals();
            } else {
                showErrorMessage("Please login first");
            }
        }
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
                .navigate(R.id.action_favoritesFragment_to_registerFregment, null, navOptions);
    }

    @Override
    public void onClick(Meal meal) {
        if (meal != null && meal.getIdMeal() != null) {
            FavoritesFragmentDirections.ActionFavoritesFragmentToMealFragment action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToMealFragment(null, meal.getIdMeal());
            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    @Override
    public void onRemoveFromFavorite(Meal meal) {
        if (presenter != null) {
            String userId = authRepository.getUserId();
            presenter.deleteFavoriteMeal(FavoriteMapper.fromMeal(meal, userId));
        }
    }

    @Override
    public void showFavoriteMeals(List<Meal> meals) {
        if (meals != null && !meals.isEmpty()) {
            adapter.setMealList(meals);
            hideEmptyState();
        } else {
            showEmptyState();
        }
    }

    @Override
    public void showSuccessMessage(String message) {
        CustomSnackbar.showError(requireView(),  message);
        if (presenter != null) {
            presenter.getFavoriteMeals();
        }
    }

    @Override
    public void showErrorMessage(String message) {
        CustomSnackbar.showError(requireView(),  message);
    }

    @Override
    public void showLoading() {
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.GONE);
        }
        if (llEmptyState != null) {
            llEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showEmptyState() {
        if (llEmptyState != null) {
            llEmptyState.setVisibility(View.VISIBLE);
        }
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideEmptyState() {
        if (llEmptyState != null) {
            llEmptyState.setVisibility(View.GONE);
        }
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}