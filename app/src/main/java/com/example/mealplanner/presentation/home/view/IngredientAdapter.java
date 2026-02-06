package com.example.mealplanner.presentation.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealplanner.R;
import com.example.mealplanner.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingrediant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgIngredient);
            name = itemView.findViewById(R.id.txtIngredientName);
            amount = itemView.findViewById(R.id.txtIngredientAmount);
        }

        void bind(Ingredient ingredient) {
            name.setText(ingredient.getName());
            amount.setText(ingredient.getAmount());

            Glide.with(itemView.getContext())
                    .load(ingredient.getImage())
                    .into(image);
        }
    }
}
