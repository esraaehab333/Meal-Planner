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
import com.example.mealplanner.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnCatecoryClick listener;

    public CategoryListAdapter(OnCatecoryClick listener) {
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
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private ImageView imgCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.categoryName);
            imgCategory = itemView.findViewById(R.id.categoryIcon);
        }
        public void bind(Category category) {
            if (category.getStrCategory() != null) {
                tvCategoryName.setText(category.getStrCategory());
            }
            if (category.getStrCategoryThumb() != null) {
                Glide.with(itemView.getContext())
                        .load(category.getStrCategoryThumb())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgCategory);
                imgCategory.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.OnCatecoryClick(category);
                    }
                });
            }
        }
    }
}