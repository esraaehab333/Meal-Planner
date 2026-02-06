package com.example.mealplanner.presentation.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealplanner.R;
import com.example.mealplanner.models.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private List<Instruction> instructions = new ArrayList<>();

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
        notifyDataSetChanged();
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
        Instruction instruction = instructions.get(position);
        holder.txtStepNumber.setText(String.valueOf(instruction.getStepNumber()));
        holder.txtStepText.setText(instruction.getStepText());
    }

    @Override
    public int getItemCount() {
        return instructions.size();
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
