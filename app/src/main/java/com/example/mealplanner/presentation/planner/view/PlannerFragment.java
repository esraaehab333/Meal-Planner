package com.example.mealplanner.presentation.planner.view;

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
import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.datasource.reposatory.AuthRepository;
import com.example.mealplanner.datasource.reposatory.AuthRepositoryImpl;
import com.example.mealplanner.datasource.reposatory.MealRepository;
import com.example.mealplanner.datasource.reposatory.MealRepositoryImpl;
import com.example.mealplanner.presentation.planner.presenter.PlannerPresenter;
import com.example.mealplanner.presentation.planner.presenter.PlannerPresenterImp;
import com.example.mealplanner.utils.CustomSnackbar;
import com.example.mealplanner.utils.mapper.PlanMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlannerFragment extends Fragment implements PlannerView {

    private CalendarView calendarView;
    private TextView tvMealName, tvAreaCategory, tvTag;
    private ImageView imgMeal;
    private Button btnViewRecipe;
    private ImageButton btnDelete;
    private CardView mealCard;
    private LinearLayout emptyStateLayout;

    private PlannerPresenter presenter;
    private String selectedDate;
    private PlanEntity currentPlanEntity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_planner, container, false);

        AuthRepository authRepository =
                new AuthRepositoryImpl(requireActivity().getApplication());
        String userId = authRepository.getUserId();

        MealRepository mealRepository =
                new MealRepositoryImpl(requireActivity().getApplication(), userId);

        presenter = new PlannerPresenterImp(this, mealRepository, userId);

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
        btnDelete = view.findViewById(R.id.btnDelete);
        mealCard = view.findViewById(R.id.cvMealCard);

        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(
                    Locale.getDefault(),
                    "%04d-%02d-%02d",
                    year,
                    month + 1,
                    dayOfMonth
            );

            presenter.loadPlannedMealsForDate(selectedDate);
        });
    }

    private String getCurrentDate() {
        return new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());
    }

    @Override
    public void showPlannedMeals(List<PlanEntity> meals) {

        if (meals != null && !meals.isEmpty()) {

            currentPlanEntity = meals.get(0);
            displayMeal(currentPlanEntity);

            mealCard.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);

        } else {

            currentPlanEntity = null;
            mealCard.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void displayMeal(PlanEntity planEntity) {

        tvMealName.setText(planEntity.strMeal);
        tvAreaCategory.setText(
                planEntity.strArea + " • " + planEntity.strCategory
        );

        if (planEntity.strTags != null && !planEntity.strTags.isEmpty()) {

            tvTag.setText(
                    planEntity.strTags
                            .split(",")[0]
                            .trim()
                            .toUpperCase()
            );

            tvTag.setVisibility(View.VISIBLE);

        } else {

            tvTag.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(planEntity.strMealThumb)
                .into(imgMeal);
        btnDelete.setOnClickListener(v -> {

            if (currentPlanEntity != null) {

                presenter.removeMealFromPlan(currentPlanEntity);

                mealCard.setVisibility(View.GONE);
                emptyStateLayout.setVisibility(View.VISIBLE);

                currentPlanEntity = null;
            }
        });

        btnViewRecipe.setOnClickListener(v -> {

            Meal meal = PlanMapper.toMeal(planEntity);

            PlannerFragmentDirections
                    .ActionPlannerFragmentToMealFragment action =
                    PlannerFragmentDirections
                            .actionPlannerFragmentToMealFragment(
                                    null,
                                    meal.getIdMeal()
                            );

            NavHostFragment
                    .findNavController(this)
                    .navigate(action);
        });
    }

    @Override
    public void showSuccessMessage(String message) {
        CustomSnackbar.showSuccess(requireView(), message);
        presenter.loadPlannedMealsForDate(selectedDate);
    }

    @Override
    public void showErrorMessage(String message) {
        CustomSnackbar.showError(requireView(), message);
    }

    @Override public void showLoading() {}
    @Override public void hideLoading() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
    }
}
