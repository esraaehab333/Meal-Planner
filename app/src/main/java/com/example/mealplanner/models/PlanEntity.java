package com.example.mealplanner.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_meals")
public class PlanEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPlan")
    public int planId;
    @NonNull
    @ColumnInfo(name = "date")
    public String date;
    @NonNull
    @ColumnInfo(name = "idMeal")
    public String idMeal;

    @NonNull
    @ColumnInfo(name = "userId")
    public String userId;

    @ColumnInfo(name = "strMeal")
    public String strMeal;

    @ColumnInfo(name = "strMealThumb")
    public String strMealThumb;

    @ColumnInfo(name = "strCategory")
    public String strCategory;

    @ColumnInfo(name = "strArea")
    public String strArea;

    @ColumnInfo(name = "strTags")
    public String strTags;

    @ColumnInfo(name = "strYoutube")
    public String strYoutube;

    @ColumnInfo(name = "strInstructions")
    public String strInstructions;

    @ColumnInfo(name = "strIngredient1") public String strIngredient1;
    @ColumnInfo(name = "strIngredient2") public String strIngredient2;
    @ColumnInfo(name = "strIngredient3") public String strIngredient3;
    @ColumnInfo(name = "strIngredient4") public String strIngredient4;
    @ColumnInfo(name = "strIngredient5") public String strIngredient5;
    @ColumnInfo(name = "strIngredient6") public String strIngredient6;
    @ColumnInfo(name = "strIngredient7") public String strIngredient7;
    @ColumnInfo(name = "strIngredient8") public String strIngredient8;
    @ColumnInfo(name = "strIngredient9") public String strIngredient9;
    @ColumnInfo(name = "strIngredient10") public String strIngredient10;
    @ColumnInfo(name = "strIngredient11") public String strIngredient11;
    @ColumnInfo(name = "strIngredient12") public String strIngredient12;
    @ColumnInfo(name = "strIngredient13") public String strIngredient13;
    @ColumnInfo(name = "strIngredient14") public String strIngredient14;
    @ColumnInfo(name = "strIngredient15") public String strIngredient15;
    @ColumnInfo(name = "strIngredient16") public String strIngredient16;
    @ColumnInfo(name = "strIngredient17") public String strIngredient17;
    @ColumnInfo(name = "strIngredient18") public String strIngredient18;
    @ColumnInfo(name = "strIngredient19") public String strIngredient19;
    @ColumnInfo(name = "strIngredient20") public String strIngredient20;

    @ColumnInfo(name = "strMeasure1") public String strMeasure1;
    @ColumnInfo(name = "strMeasure2") public String strMeasure2;
    @ColumnInfo(name = "strMeasure3") public String strMeasure3;
    @ColumnInfo(name = "strMeasure4") public String strMeasure4;
    @ColumnInfo(name = "strMeasure5") public String strMeasure5;
    @ColumnInfo(name = "strMeasure6") public String strMeasure6;
    @ColumnInfo(name = "strMeasure7") public String strMeasure7;
    @ColumnInfo(name = "strMeasure8") public String strMeasure8;
    @ColumnInfo(name = "strMeasure9") public String strMeasure9;
    @ColumnInfo(name = "strMeasure10") public String strMeasure10;
    @ColumnInfo(name = "strMeasure11") public String strMeasure11;
    @ColumnInfo(name = "strMeasure12") public String strMeasure12;
    @ColumnInfo(name = "strMeasure13") public String strMeasure13;
    @ColumnInfo(name = "strMeasure14") public String strMeasure14;
    @ColumnInfo(name = "strMeasure15") public String strMeasure15;
    @ColumnInfo(name = "strMeasure16") public String strMeasure16;
    @ColumnInfo(name = "strMeasure17") public String strMeasure17;
    @ColumnInfo(name = "strMeasure18") public String strMeasure18;
    @ColumnInfo(name = "strMeasure19") public String strMeasure19;
    @ColumnInfo(name = "strMeasure20") public String strMeasure20;
    public PlanEntity() {
    }
    @NonNull
    public String getMealId() { return idMeal; }
    public void setMealId(String mealId) { this.idMeal = mealId; }

    public String getMealName() { return strMeal; }
    public void setMealName(String mealName) { this.strMeal = mealName; }

    public String getMealThumb() { return strMealThumb; }
    public void setMealThumb(String mealThumb) { this.strMealThumb = mealThumb; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    @Ignore
    public PlanEntity(
            @NonNull String date,
            @NonNull String idMeal,
            @NonNull String userId,
            String strMeal,
            String strMealThumb,
            String strCategory,
            String strArea,
            String strTags,
            String strYoutube,
            String strInstructions,
            String[] ingredients,
            String[] measures
    ) {
        this.date = date;
        this.idMeal = idMeal;
        this.userId = userId;

        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strTags = strTags;
        this.strYoutube = strYoutube;
        this.strInstructions = strInstructions;

        this.strIngredient1 = ingredients.length > 0 ? ingredients[0] : null;
        this.strIngredient2 = ingredients.length > 1 ? ingredients[1] : null;
        this.strIngredient3 = ingredients.length > 2 ? ingredients[2] : null;
        this.strIngredient4 = ingredients.length > 3 ? ingredients[3] : null;
        this.strIngredient5 = ingredients.length > 4 ? ingredients[4] : null;
        this.strIngredient6 = ingredients.length > 5 ? ingredients[5] : null;
        this.strIngredient7 = ingredients.length > 6 ? ingredients[6] : null;
        this.strIngredient8 = ingredients.length > 7 ? ingredients[7] : null;
        this.strIngredient9 = ingredients.length > 8 ? ingredients[8] : null;
        this.strIngredient10 = ingredients.length > 9 ? ingredients[9] : null;
        this.strIngredient11 = ingredients.length > 10 ? ingredients[10] : null;
        this.strIngredient12 = ingredients.length > 11 ? ingredients[11] : null;
        this.strIngredient13 = ingredients.length > 12 ? ingredients[12] : null;
        this.strIngredient14 = ingredients.length > 13 ? ingredients[13] : null;
        this.strIngredient15 = ingredients.length > 14 ? ingredients[14] : null;
        this.strIngredient16 = ingredients.length > 15 ? ingredients[15] : null;
        this.strIngredient17 = ingredients.length > 16 ? ingredients[16] : null;
        this.strIngredient18 = ingredients.length > 17 ? ingredients[17] : null;
        this.strIngredient19 = ingredients.length > 18 ? ingredients[18] : null;
        this.strIngredient20 = ingredients.length > 19 ? ingredients[19] : null;

        this.strMeasure1 = measures.length > 0 ? measures[0] : null;
        this.strMeasure2 = measures.length > 1 ? measures[1] : null;
        this.strMeasure3 = measures.length > 2 ? measures[2] : null;
        this.strMeasure4 = measures.length > 3 ? measures[3] : null;
        this.strMeasure5 = measures.length > 4 ? measures[4] : null;
        this.strMeasure6 = measures.length > 5 ? measures[5] : null;
        this.strMeasure7 = measures.length > 6 ? measures[6] : null;
        this.strMeasure8 = measures.length > 7 ? measures[7] : null;
        this.strMeasure9 = measures.length > 8 ? measures[8] : null;
        this.strMeasure10 = measures.length > 9 ? measures[9] : null;
        this.strMeasure11 = measures.length > 10 ? measures[10] : null;
        this.strMeasure12 = measures.length > 11 ? measures[11] : null;
        this.strMeasure13 = measures.length > 12 ? measures[12] : null;
        this.strMeasure14 = measures.length > 13 ? measures[13] : null;
        this.strMeasure15 = measures.length > 14 ? measures[14] : null;
        this.strMeasure16 = measures.length > 15 ? measures[15] : null;
        this.strMeasure17 = measures.length > 16 ? measures[16] : null;
        this.strMeasure18 = measures.length > 17 ? measures[17] : null;
        this.strMeasure19 = measures.length > 18 ? measures[18] : null;
        this.strMeasure20 = measures.length > 19 ? measures[19] : null;
    }

}
