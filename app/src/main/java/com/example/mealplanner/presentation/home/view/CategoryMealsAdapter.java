package com.example.mealplanner.presentation.home.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mealplanner.R;
import com.example.mealplanner.data.models.Meal;
import com.facebook.shimmer.ShimmerFrameLayout; // تأكدي من عمل import
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.List;

public class CategoryMealsAdapter extends RecyclerView.Adapter<CategoryMealsAdapter.ViewHolder> {

    private List<Meal> meals = new ArrayList<>();
    private final OnMealClick listener;

    public CategoryMealsAdapter(OnMealClick listener) {
        this.listener = listener;
    }

    public void setMealList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvName.setText(meal.getStrMeal());
        holder.tvCategory.setText(meal.getStrCategory());
        holder.shimmerLayout.startShimmer();
        holder.shimmerLayout.setVisibility(View.VISIBLE);

        Glide.with(holder.itemView.getContext())
                .load(meal.getStrMealThumb())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.shimmerLayout.stopShimmer();
                        holder.shimmerLayout.hideShimmer();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.shimmerLayout.stopShimmer();
                        holder.shimmerLayout.hideShimmer();
                        return false;
                    }
                })
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivImage;
        TextView tvName, tvCategory;
        ShimmerFrameLayout shimmerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivMealImage);
            tvName = itemView.findViewById(R.id.tvMealName);
            tvCategory = itemView.findViewById(R.id.tvAreaCategory);
            shimmerLayout = itemView.findViewById(R.id.shimmer_view_container); // ربطه بالـ ID في الـ XML
        }
    }
}