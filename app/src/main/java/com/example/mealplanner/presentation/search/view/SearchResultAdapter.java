package com.example.mealplanner.presentation.search.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<Meal> mealList = new ArrayList<>();
    private final OnMealSearchClick listener;

    public SearchResultAdapter(OnMealSearchClick listener) {
        this.listener = listener;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = (mealList == null) ? new ArrayList<>() : mealList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return mealList == null ? 0 : mealList.size();
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private final TextView mealName, areaAndCategory;
        private final ImageView mealImage;
        private final ShimmerFrameLayout shimmerContainer;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.tvMealName);
            mealImage = itemView.findViewById(R.id.ivMealImage);
            areaAndCategory = itemView.findViewById(R.id.tvAreaCategory);
            shimmerContainer = itemView.findViewById(R.id.shimmer_view_container);
        }

        public void bind(Meal meal) {
            if (meal == null) return;

            mealName.setText(meal.getStrMeal());

            // shimmer
            if (shimmerContainer != null) {
                shimmerContainer.startShimmer();
                shimmerContainer.setShimmer(new com.facebook.shimmer.Shimmer.AlphaHighlightBuilder().build());
            }

            Glide.with(itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (shimmerContainer != null) {
                                shimmerContainer.stopShimmer();
                                shimmerContainer.setShimmer(null);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (shimmerContainer != null) {
                                shimmerContainer.stopShimmer();
                                shimmerContainer.setShimmer(null);
                            }
                            return false;
                        }
                    })
                    .into(mealImage);

            String text = "";
            if (meal.getStrArea() != null && !meal.getStrArea().trim().isEmpty()) text += meal.getStrArea();
            if (meal.getStrCategory() != null && !meal.getStrCategory().trim().isEmpty()) {
                if (!text.isEmpty()) text += " • ";
                text += meal.getStrCategory();
            }
            areaAndCategory.setText(text);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMealClick(meal);
            });
        }
    }
}
