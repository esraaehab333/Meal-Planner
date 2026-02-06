package com.example.mealplanner.presentation.home.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.models.Ingredient;
import com.example.mealplanner.models.Instruction;

import java.util.ArrayList;
import java.util.List;

public class MealFragment extends Fragment {

    RecyclerView rvIngredients, rvInstructions;
    IngredientAdapter ingredientAdapter;
    InstructionAdapter instructionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        rvIngredients = view.findViewById(R.id.rvIngredients);
        rvInstructions = view.findViewById(R.id.rvInstructions);

        ingredientAdapter = new IngredientAdapter();
        instructionAdapter = new InstructionAdapter();

        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvIngredients.setAdapter(ingredientAdapter);

        rvInstructions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInstructions.setAdapter(instructionAdapter);

        loadFakeIngredients();
        loadFakeInstructions();

        return view;
    }

    private void loadFakeIngredients() {
        List<Ingredient> fakeList = new ArrayList<>();
        fakeList.add(new Ingredient("Soy Sauce", "3/4 cup", "https://www.themealdb.com/images/ingredients/Soy%20Sauce.png"));
        fakeList.add(new Ingredient("Water", "1/2 cup", "https://www.themealdb.com/images/ingredients/Water.png"));
        fakeList.add(new Ingredient("Brown Sugar", "1/4 cup", "https://www.themealdb.com/images/ingredients/Brown%20Sugar.png"));
        fakeList.add(new Ingredient("Chicken Breasts", "2 pcs", "https://www.themealdb.com/images/ingredients/Chicken.png"));

        ingredientAdapter.setIngredients(fakeList);
    }

    private void loadFakeInstructions() {
        String instructionsText = "Preheat oven to 350° F." +
                "Combine soy sauce, ½ cup water, brown sugar, ginger and garlic in a small saucepan and cover. Bring to a boil over medium heat. Remove lid and cook for one minute once boiling.\n" +
                "Place the chicken breasts in the prepared pan. Pour one cup of the sauce over top of chicken. Place chicken in oven and bake 35 minutes or until cooked through. Remove from oven and shred chicken in the dish using two forks.\n" ;
        String[] steps = instructionsText.split("\\.\n|\\. "); // split into steps
        List<Instruction> instructionList = new ArrayList<>();
        for (int i = 0; i < steps.length; i++) {
            if (!steps[i].trim().isEmpty())
                instructionList.add(new Instruction(i + 1, steps[i].trim() + "."));
        }

        instructionAdapter.setInstructions(instructionList);
    }
}
