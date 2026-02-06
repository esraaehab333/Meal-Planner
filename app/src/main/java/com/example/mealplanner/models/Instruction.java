package com.example.mealplanner.models;

public class Instruction {
    private int stepNumber;
    private String stepText;

    public Instruction(int stepNumber, String stepText) {
        this.stepNumber = stepNumber;
        this.stepText = stepText;
    }

    public int getStepNumber() { return stepNumber; }
    public String getStepText() { return stepText; }
}
