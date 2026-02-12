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
import com.example.mealplanner.data.models.Area;
import com.example.mealplanner.utils.FlagUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchAreaAdapter extends RecyclerView.Adapter<SearchAreaAdapter.AreaViewHolder> {

    private List<Area> areaList = new ArrayList<>();
    private OnAreaClickListener listener;

    public interface OnAreaClickListener {
        void onAreaClick(Area area);
    }

    public SearchAreaAdapter(OnAreaClickListener listener) {
        this.listener = listener;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areaList.get(position);
        holder.bind(area);
    }

    @Override
    public int getItemCount() {
        return areaList != null ? areaList.size() : 0;
    }

    class AreaViewHolder extends RecyclerView.ViewHolder {
        private TextView areaName;
        private ImageView imgAreaFlag;
        private ShimmerFrameLayout shimmerContainer;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            areaName = itemView.findViewById(R.id.areaName);
            imgAreaFlag = itemView.findViewById(R.id.imgAreaFlag);
            shimmerContainer = itemView.findViewById(R.id.shimmer_view_container);
        }

        public void bind(Area area) {
            areaName.setText(area.getStrArea());
            shimmerContainer.startShimmer();
            shimmerContainer.setVisibility(View.VISIBLE);
            String flagUrl = FlagUtils.getFlagUrl(area.getStrArea());
            Glide.with(itemView.getContext())
                    .load(flagUrl)
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
                    .into(imgAreaFlag);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAreaClick(area);
                }
            });
        }
        private void stopLoadingEffect() {
            shimmerContainer.stopShimmer();
            shimmerContainer.setShimmer(null);
        }
    }
}