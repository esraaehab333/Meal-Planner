package com.example.mealplanner.presentation.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mealplanner.R;
import com.example.mealplanner.data.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnCategoryClick listener;
    public CategoryAdapter(OnCategoryClick listener) {
        this.categoryList = new ArrayList<>();
        this.listener = listener;
    }
    public void setCategoryList(List<Category> categoryList) {
        if (categoryList != null) {
            this.categoryList = categoryList;
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_category_item_list, parent, false);
        return new CategoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category, listener);
    }
    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private ImageView recipeImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.ivMealImage);
            recipeName = itemView.findViewById(R.id.tvMealName);
        }

        public void bind(Category category, OnCategoryClick listener) {
            if (category.getStrCategory() != null) {
                recipeName.setText(category.getStrCategory());
            }
            if (category.getStrCategoryThumb() != null) {
                Glide.with(itemView.getContext())
                        .load(category.getStrCategoryThumb())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .into(recipeImage);
            }
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category, getAdapterPosition());
                }
            });
        }
    }
    public interface OnCategoryClick {
        void onCategoryClick(Category category, int position);
    }
}