package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.models.Ingredient;
import com.example.mealplanner.models.Instruction;
import com.example.mealplanner.models.Meal;
import com.example.mealplanner.presentation.home.presenter.MealPresenter;
import com.example.mealplanner.presentation.home.presenter.MealPresenterImp;

import java.util.ArrayList;
import java.util.List;

public class MealFragment extends Fragment implements MealView {

    RecyclerView rvIngredients, rvInstructions;
    ImageView mealImage;
    TextView mealName, mealCountry, mealTag, mealCategory;
    IngredientAdapter ingredientAdapter;
    InstructionAdapter instructionAdapter;

    private ImageButton favoriteBtn, backBtn;
    private MealPresenter presenter;
    private boolean isFavorite = false;
    private Meal currentMeal;

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
            Toast.makeText(requireContext(), "Meal data not found", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return view;
        }
        presenter = new MealPresenterImp(this, requireContext());
        initViews(view);
        setupRecyclerViews();
        setupButtons();
        presenter.loadMeal(currentMeal);
        presenter.isFavorite(currentMeal.getIdMeal());
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
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
    }

    private void setupButtons() {
        backBtn.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        favoriteBtn.setOnClickListener(v -> {
            if (isFavorite) {
                presenter.deleteFromFav(currentMeal);
            } else {
                presenter.addToFav(currentMeal);
            }
        });
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    private void loadIngredientsFromMeal(Meal meal) {
        List<Ingredient> ingredientList = new ArrayList<>();
        List<String> names = meal.getIngredientsList();
        List<String> amounts = meal.getMeasuresList();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String amount = i < amounts.size() ? amounts.get(i) : "";
            String image = "https://www.themealdb.com/images/ingredients/" + name.replace(" ", "%20") + ".png";
            ingredientList.add(new Ingredient(name, amount, image));
        }
        ingredientAdapter.setIngredients(ingredientList);
    }

    private void loadInstructionsFromMeal(Meal meal) {
        List<Instruction> instructionList = new ArrayList<>();
        if (meal.getStrInstructions() != null && !meal.getStrInstructions().isEmpty()) {
            String normalized = meal.getStrInstructions()
                    .replace("\r\n", "\n")
                    .replace("\r", "\n");
            String[] steps;
            if (normalized.toLowerCase().contains("step")) {
                steps = normalized.split("(?i)step \\d+\\s*\\n");
            } else if (normalized.matches("(?s).*\\d+\\n.*")) {
                steps = normalized.split("\\d+\\n");
            } else {
                steps = normalized.split("\\.\\s+");
            }
            int index = 1;
            for (String step : steps) {
                String trimmed = step.trim();
                if (!trimmed.isEmpty()) {
                    if (!trimmed.endsWith(".")) {
                        trimmed += ".";
                    }
                    instructionList.add(new Instruction(index++, trimmed));
                }
            }
        }
        instructionAdapter.setInstructions(instructionList);
    }
}