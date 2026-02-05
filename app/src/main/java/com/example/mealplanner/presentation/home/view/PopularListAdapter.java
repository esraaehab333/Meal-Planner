package com.example.mealplanner.presentation.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealplanner.R;
import com.example.mealplanner.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.PopularViewHolder> {

    private List<Meal> mealList = new ArrayList<>();

    public void setMealList(List<Meal> mealList) {
        if (mealList != null) {
            this.mealList = mealList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_meal_item_list, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        holder.bind(mealList.get(position));
    }

    @Override
    public int getItemCount() {
        return mealList == null ? 0 : mealList.size();
    }

    class PopularViewHolder extends RecyclerView.ViewHolder {

        TextView tvMealName, tvTag, tvAreaCategory;
        ImageView imgMeal;
        Button btnViewRecipe;
        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            tvTag = itemView.findViewById(R.id.tvTag);
            tvAreaCategory = itemView.findViewById(R.id.tvAreaCategory);
            imgMeal = itemView.findViewById(R.id.imgMeal);
            btnViewRecipe = itemView.findViewById(R.id.btnViewRecipe);
        }

        void bind(Meal meal) {
            tvMealName.setText(meal.getStrMeal());
            if (meal.getStrTags() != null && !meal.getStrTags().isEmpty()) {
                String[] tagsArray = meal.getStrTags().split(",");
                if (tagsArray.length > 0) {
                    tvTag.setText(tagsArray[0].trim().toUpperCase());
                    tvTag.setVisibility(View.VISIBLE);
                }
            } else {
                tvTag.setVisibility(View.GONE);
            }
            StringBuilder areaCategory = new StringBuilder();

            if (meal.getStrArea() != null) {
                areaCategory.append(meal.getStrArea().toUpperCase());
            }
            if (meal.getStrCategory() != null) {
                if (areaCategory.length() > 0) {
                    areaCategory.append(" • ");
                }
                areaCategory.append(meal.getStrCategory().toUpperCase());
            }
            // TAG
            if (meal.getStrTags() != null && !meal.getStrTags().isEmpty()) {
                tvTag.setText(meal.getStrTags().toUpperCase());
                tvTag.setVisibility(View.VISIBLE);
            } else {
                tvTag.setVisibility(View.GONE);
            }

            tvAreaCategory.setText(areaCategory.toString());
            Glide.with(itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.img_meal_test)
                    .error(R.drawable.img_meal_test)
                    .into(imgMeal);
        }
    }
}
