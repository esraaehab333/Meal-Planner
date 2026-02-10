package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.presenter.FavoritePresenter;
import com.example.mealplanner.presentation.home.presenter.FavoritePresenterImp;
import com.example.mealplanner.utils.CustomDialog;
import com.example.mealplanner.utils.CustomSnackbar;

import java.util.List;

public class FavoritesFragment extends Fragment implements OnFavoriteClick, FavoriteView {

    private RecyclerView rvFavorites;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FavoriteListAdapter adapter;
    private FavoritePresenter presenter;
    private SharedPreferanceDao sharedPref;

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
        //progressBar = view.findViewById(R.id.progressBar);
        //tvEmptyState = view.findViewById(R.id.tvEmptyState);
    }

    private void setupRecyclerView() {
        adapter = new FavoriteListAdapter(this);
        rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFavorites.setAdapter(adapter);
    }

    private void checkUserAndLoadFavorites() {
        sharedPref = new SharedPreferanceLocalDataSource(requireContext());
        String currentUserId = sharedPref.getUserId();

        if ("GUEST".equals(currentUserId)) {
            CustomDialog.showGuestDialog(this);
        } else {
            if (currentUserId != null) {
                FavoriteLocalDataSource localDataSource =
                        new FavoriteLocalDataSource(requireContext(), currentUserId);
                presenter = new FavoritePresenterImp(this, localDataSource);
                presenter.getFavoriteMeals();
            } else {
                // Navigate to login screen
                showErrorMessage("Please login first");
            }
        }
    }

    @Override
    public void onClick(Meal meal) {
        FavoritesFragmentDirections.ActionFavoritesFragmentToMealFragment action =
                FavoritesFragmentDirections.actionFavoritesFragmentToMealFragment(meal);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onRemoveFromFavorite(Meal meal) {
        if (presenter != null) {
            presenter.deleteFavoriteMeal(FavoriteMapper.fromMeal(meal,sharedPref.getUserId()));
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
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEmptyState() {
        if (tvEmptyState != null) {
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText("No favorite meals");
        }
        if (rvFavorites != null) {
            rvFavorites.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideEmptyState() {
        if (tvEmptyState != null) {
            tvEmptyState.setVisibility(View.GONE);
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