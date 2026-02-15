package com.example.mealplanner.presentation.meal.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.datasource.auth.local.SharedPreferanceLocalDataSource;
import com.example.mealplanner.datasource.plan.local.PlanLocalDataSource;
import com.example.mealplanner.data.models.IngredientMealDetails;
import com.example.mealplanner.data.models.Instruction;
import com.example.mealplanner.data.models.Meal;
import com.example.mealplanner.data.enitiy.PlanEntity;
import com.example.mealplanner.presentation.meal.presenter.MealPresenter;
import com.example.mealplanner.presentation.meal.presenter.MealPresenterImp;
import com.example.mealplanner.presentation.planner.presenter.PlannerPresenter;
import com.example.mealplanner.presentation.planner.presenter.PlannerPresenterImp;
import com.example.mealplanner.presentation.planner.view.PlannerView;
import com.example.mealplanner.utils.CustomDialog;
import com.example.mealplanner.utils.CustomSnackbar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MealFragment extends Fragment implements MealView {

    RecyclerView rvIngredients, rvInstructions;
    ImageView mealImage;
    TextView mealName, mealCountry, mealTag, mealCategory;
    IngredientAdapter ingredientAdapter;
    InstructionAdapter instructionAdapter;

    private ImageButton favoriteBtn, backBtn;
    private Button btnSetMealForDay;
    private MealPresenter presenter;
    private PlannerPresenter plannerPresenter;
    private boolean isFavorite = false;
    private Meal currentMeal;
    private YouTubePlayerView playerView;
    private YouTubePlayer myYouTubePlayer = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);
        if (getArguments() != null) {
            try {
                MealFragmentArgs args = MealFragmentArgs.fromBundle(getArguments());
                currentMeal = args.getMeal();
            } catch (Exception e) {
                currentMeal = (Meal) getArguments().getSerializable("meal");
            }
        }
        if (currentMeal == null) {
            CustomSnackbar.showError(requireView(), "Meal data not found");
            requireActivity().onBackPressed();
            return view;
        }
        presenter = new MealPresenterImp(this, requireContext());
        initializePlannerPresenter();
        initViews(view);
        setupRecyclerViews();
        setupButtons();
        presenter.loadMeal(currentMeal);
        presenter.isFavorite(currentMeal.getIdMeal());
        return view;
    }

    private void initializePlannerPresenter() {
        SharedPreferanceLocalDataSource sharedPref = new SharedPreferanceLocalDataSource(requireContext());
        String userId = sharedPref.getUserId();

        Log.d("PLANNER_DEBUG", "Initialized Planner for UserID: " + userId);

        PlanLocalDataSource localDataSource = new PlanLocalDataSource(requireContext(), userId);

        plannerPresenter = new PlannerPresenterImp(new PlannerView() {
            @Override
            public void showPlannedMeals(List<PlanEntity> meals) {}
            @Override
            public void showSuccessMessage(String message) {
                if (isAdded() && getContext() != null) {
                    CustomSnackbar.showSuccess(requireView(), message);
                }
            }
            @Override
            public void showErrorMessage(String message) {
                if (isAdded() && getContext() != null) {
                    CustomSnackbar.showError(requireView(), message);
                    Log.e("PLANNER_DEBUG", "Error from Presenter: " + message);
                }
            }
            @Override public void showLoading() {}
            @Override public void hideLoading() {}
        }, localDataSource, userId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.onDestroy();
        if (plannerPresenter != null) plannerPresenter.onDestroy();
    }

    private void initViews(View view) {
        rvIngredients = view.findViewById(R.id.rvIngredients);
        rvInstructions = view.findViewById(R.id.rvInstructions);
        mealImage = view.findViewById(R.id.imgMeal);
        mealTag = view.findViewById(R.id.mealTag);
        mealName = view.findViewById(R.id.txtMealName);
        mealCategory = view.findViewById(R.id.mealCategory);
        mealCountry = view.findViewById(R.id.mealCountry);
        favoriteBtn = view.findViewById(R.id.btn_favorite);
        backBtn = view.findViewById(R.id.btn_back);
        btnSetMealForDay = view.findViewById(R.id.btnSetMealForDay);
        playerView = view.findViewById(R.id.youtube_player_view);
    }

    private void setupButtons() {
        backBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        favoriteBtn.setOnClickListener(v -> {
            SharedPreferanceLocalDataSource sharedPref = new SharedPreferanceLocalDataSource(requireContext());
            String userId = sharedPref.getUserId();

            if ("GUEST".equals(userId)) {
                showGuestLimitationDialog();
                return;
            }

            if (isFavorite) presenter.deleteFromFav(currentMeal);
            else presenter.addToFav(currentMeal);
        });

        btnSetMealForDay.setOnClickListener(v -> {
            SharedPreferanceLocalDataSource sharedPref = new SharedPreferanceLocalDataSource(requireContext());
            String userId = sharedPref.getUserId();

            if ("GUEST".equals(userId)) {
                showGuestLimitationDialog();
                return;
            }
            showDatePickerDialog();
        });

        playerView.setOnClickListener(v -> {
            if (myYouTubePlayer != null) myYouTubePlayer.play();
        });
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
                .navigate(R.id.action_mealFragment_to_loginFregment, null, navOptions);
    }

    public void showMealVideo(String videoId) {
        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                myYouTubePlayer = youTubePlayer;
                myYouTubePlayer.cueVideo(videoId, 0);
            }
        });
    }

    private String extractYoutubeId(String youtubeUrl) {
        if (youtubeUrl == null || youtubeUrl.isEmpty()) return null;
        if (youtubeUrl.contains("v=")) return youtubeUrl.substring(youtubeUrl.indexOf("v=") + 2);
        else if (youtubeUrl.contains("youtu.be/")) return youtubeUrl.substring(youtubeUrl.lastIndexOf("/") + 1);
        return youtubeUrl;
    }

    private void showDatePickerDialog() {
        if (!isAdded() || getContext() == null) return;
        initializePlannerPresenter();
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                R.style.CustomCalendarTheme,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    if (plannerPresenter != null && currentMeal != null) {
                        plannerPresenter.addMealToPlan(currentMeal, selectedDate);
                    } else {
                        showErrorMessage("Error: Unable to add meal");
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void setupRecyclerViews() {
        ingredientAdapter = new IngredientAdapter();
        instructionAdapter = new InstructionAdapter();
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientAdapter);
        rvInstructions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInstructions.setAdapter(instructionAdapter);
    }

    @Override
    public void showMeal(Meal meal) {
        mealName.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealCountry.setText(meal.getStrArea());

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .placeholder(R.drawable.img_meal_test)
                .error(R.drawable.img_meal_test)
                .into(mealImage);

        if (meal.getStrTags() != null && !meal.getStrTags().isEmpty()) {
            String[] tagsArray = meal.getStrTags().split(",");
            mealTag.setText(tagsArray[0].trim().toUpperCase());
            mealTag.setVisibility(View.VISIBLE);
        } else {
            mealTag.setVisibility(View.GONE);
        }

        if (meal.getStrYoutube() != null) {
            showMealVideo(extractYoutubeId(meal.getStrYoutube()));
        }

        loadIngredientsFromMeal(meal);
        loadInstructionsFromMeal(meal);
    }

    @Override
    public void updateFavoriteStatus(boolean isFavorite) {
        this.isFavorite = isFavorite;
        int iconRes = isFavorite ? R.drawable.ic_fulled_heart : R.drawable.ic_soild_heart;
        favoriteBtn.setImageResource(iconRes);
    }

    @Override
    public void showSuccessMessage(String message) {
        CustomSnackbar.showSuccess(requireView(), message);
    }

    @Override
    public void showErrorMessage(String message) {
        CustomSnackbar.showError(requireView(), message);
    }

    @Override public void showLoading() {}
    @Override public void hideLoading() {}

    private void loadIngredientsFromMeal(Meal meal) {
        List<IngredientMealDetails> ingredientList = new ArrayList<>();
        List<String> names = meal.getIngredientsList();
        List<String> amounts = meal.getMeasuresList();
        if (names != null) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                if (name != null && !name.isEmpty()) {
                    String amount = (amounts != null && i < amounts.size()) ? amounts.get(i) : "";
                    String image = "https://www.themealdb.com/images/ingredients/" + name.replace(" ", "%20") + ".png";
                    ingredientList.add(new IngredientMealDetails(name, amount, image));
                }
            }
        }
        ingredientAdapter.setIngredients(ingredientList);
    }

    private void loadInstructionsFromMeal(Meal meal) {
        List<Instruction> instructionList = new ArrayList<>();
        if (meal.getStrInstructions() != null && !meal.getStrInstructions().isEmpty()) {
            String normalized = meal.getStrInstructions().replace("\r\n", "\n").replace("\r", "\n");
            String[] steps = normalized.split("\\.\\s+");
            int index = 1;
            for (String step : steps) {
                if (!step.trim().isEmpty()) {
                    instructionList.add(new Instruction(index++, step.trim() + "."));
                }
            }
        }
        instructionAdapter.setInstructions(instructionList);
    }
}