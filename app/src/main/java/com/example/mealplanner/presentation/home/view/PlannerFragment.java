package com.example.mealplanner.presentation.home.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.datasource.plan.local.PlanLocalDataSource;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.models.PlanEntity;
import com.example.mealplanner.presentation.home.presenter.PlannerPresenter;
import com.example.mealplanner.presentation.home.presenter.PlannerPresenterImp;
import com.example.mealplanner.utils.CustomSnackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlannerFragment extends Fragment implements PlannerView {

    private CalendarView calendarView;
    private TextView tvMealName, tvAreaCategory, tvTag;
    private ImageView imgMeal, ivEmptyState;
    private Button btnViewRecipe;
    private ImageButton btnFavorite;
    private CardView mealCard;
    private LinearLayout emptyStateLayout;
    private TextView tvEmptyMessage;

    private PlannerPresenter presenter;
    private String selectedDate;
    private PlanEntity currentPlanEntity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MealPlannerPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        PlanLocalDataSource localDataSource = new PlanLocalDataSource(requireContext(), userId);
        presenter = new PlannerPresenterImp(this, localDataSource, userId);
        initViews(view);
        setupCalendar();
        selectedDate = getCurrentDate();
        presenter.loadPlannedMealsForDate(selectedDate);
        return view;
    }

    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvAreaCategory = view.findViewById(R.id.tvAreaCategory);
        tvTag = view.findViewById(R.id.tvTag);
        imgMeal = view.findViewById(R.id.imgMeal);
        btnViewRecipe = view.findViewById(R.id.btnViewRecipe);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        mealCard = view.findViewById(R.id.mealCard);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        ivEmptyState = view.findViewById(R.id.ivEmptyState);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                presenter.loadPlannedMealsForDate(selectedDate);
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void showPlannedMeals(List<PlanEntity> meals) {
        if (meals != null && !meals.isEmpty()) {
            currentPlanEntity = meals.get(0);
            displayMeal(currentPlanEntity);
            showMealCard();
            hideEmptyState();
        } else {
            currentPlanEntity = null;
            hideMealCard();
            showEmptyState();
        }
    }

    private void displayMeal(PlanEntity planEntity) {
        tvMealName.setText(planEntity.strMeal);
        tvAreaCategory.setText(planEntity.strArea + " • " + planEntity.strCategory);

        if (planEntity.strTags != null && !planEntity.strTags.isEmpty()) {
            String[] tags = planEntity.strTags.split(",");
            tvTag.setText(tags[0].trim().toUpperCase());
            tvTag.setVisibility(View.VISIBLE);
        } else {
            tvTag.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(planEntity.strMealThumb)
                .placeholder(R.drawable.img_meal_test)
                .error(R.drawable.img_meal_test)
                .into(imgMeal);

        btnFavorite.setOnClickListener(v -> {
            if (currentPlanEntity != null) {
                presenter.removeMealFromPlan(currentPlanEntity);
            }
        });

        btnViewRecipe.setOnClickListener(v -> {
            Meal meal =PlanMapper.toMeal(currentPlanEntity);
            PlannerFragmentDirections.ActionPlannerFragmentToMealFragment action =
                    PlannerFragmentDirections.actionPlannerFragmentToMealFragment(meal);
            NavHostFragment.findNavController(this).navigate(action);
           // CustomSnackbar.showSuccess(requireView(), "View Recipe: " + planEntity.strMeal);
        });
    }

    private void showMealCard() {
        if (mealCard != null) {
            mealCard.setVisibility(View.VISIBLE);
        }
    }

    private void hideMealCard() {
        if (mealCard != null) {
            mealCard.setVisibility(View.GONE);
        }
    }

    private void showEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSuccessMessage(String message) {
        CustomSnackbar.showSuccess(requireView(), message);
        if (selectedDate != null) {
            presenter.loadPlannedMealsForDate(selectedDate);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        CustomSnackbar.showError(requireView(), message);
    }

    @Override
    public void showLoading() {}

    @Override
    public void hideLoading() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}