package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.models.Ingredient;
import com.example.mealplanner.models.Instruction;
import com.example.mealplanner.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class MealFragment extends Fragment {

    RecyclerView rvIngredients, rvInstructions;
    ImageView mealImage;
    TextView mealName , mealCountry , mealTag ,mealCategory;
    IngredientAdapter ingredientAdapter;
    InstructionAdapter instructionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        MealFragmentArgs args = MealFragmentArgs.fromBundle(getArguments());
        Meal meal = args.getMeal();

        rvIngredients = view.findViewById(R.id.rvIngredients);
        rvInstructions = view.findViewById(R.id.rvInstructions);
        mealImage= view.findViewById(R.id.imgMeal);
        mealTag= view.findViewById(R.id.mealTag);
        mealName= view.findViewById(R.id.txtMealName);
        mealCategory =view.findViewById(R.id.mealCategory);
        mealCountry=view.findViewById(R.id.mealCountry);

        ingredientAdapter = new IngredientAdapter();
        instructionAdapter = new InstructionAdapter();

        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientAdapter);

        rvInstructions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInstructions.setAdapter(instructionAdapter);

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

        return view;
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
                    // Add dot at the end if missing
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