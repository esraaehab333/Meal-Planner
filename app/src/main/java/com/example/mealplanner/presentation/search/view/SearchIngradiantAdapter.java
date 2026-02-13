package com.example.mealplanner.presentation.search.view;

import android.graphics.drawable.Drawable;
import android.util.Log; // تأكدي من استيراد الـ Log
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
import com.example.mealplanner.data.models.Ingredient;
import com.example.mealplanner.data.models.IngredientMealDetails;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchIngradiantAdapter extends RecyclerView.Adapter<SearchIngradiantAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList = new ArrayList<>();
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    public SearchIngradiantAdapter(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingradient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList != null ? ingredientList.size() : 0;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView ingredientName;
        private ImageView ingredientImage;
        private ShimmerFrameLayout shimmerContainer;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            shimmerContainer = itemView.findViewById(R.id.shimmer_view_container);
        }

        public void bind(Ingredient ingredient) {
            ingredientName.setText(ingredient.getStrIngredient() != null ? ingredient.getStrIngredient() : "Unknown");
            if (shimmerContainer != null) {
                shimmerContainer.startShimmer();
                shimmerContainer.setVisibility(View.VISIBLE);
            }
            String name = (ingredient.getStrIngredient() != null) ? ingredient.getStrIngredient() : "";
            String imageUrl = "https://www.themealdb.com/images/ingredients/" + name + ".png";
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            stopLoadingEffect();
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            stopLoadingEffect();
                            return false;
                        }
                    })
                    .into(ingredientImage);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIngredientClick(ingredient);
                }
            });
        }
        private void stopLoadingEffect() {
            if (shimmerContainer != null) {
                shimmerContainer.stopShimmer();
                shimmerContainer.setShimmer(null);
            }
        }
    }
}