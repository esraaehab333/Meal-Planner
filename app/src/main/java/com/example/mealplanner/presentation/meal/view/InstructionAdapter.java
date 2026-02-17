package com.example.mealplanner.presentation.meal.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.data.models.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private List<Instruction> instructions = new ArrayList<>();
    private List<Instruction> visibleInstructions = new ArrayList<>();
    private static final int MAX_VISIBLE = 4;

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
        visibleInstructions = new ArrayList<>(
                instructions.subList(0, Math.min(MAX_VISIBLE, instructions.size()))
        );
        notifyDataSetChanged();
    }

    public void showAll() {
        visibleInstructions = new ArrayList<>(instructions);
        notifyDataSetChanged();
    }

    public boolean hasMore() {
        return instructions.size() > MAX_VISIBLE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instractions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Instruction instruction = visibleInstructions.get(position);
        holder.txtStepNumber.setText(String.valueOf(instruction.getStepNumber()));
        holder.txtStepText.setText(instruction.getStepText());
    }

    @Override
    public int getItemCount() {
        return visibleInstructions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStepNumber, txtStepText;

        ViewHolder(View itemView) {
            super(itemView);
            txtStepNumber = itemView.findViewById(R.id.txtStepNumber);
            txtStepText = itemView.findViewById(R.id.txtStepText);
        }
    }
}