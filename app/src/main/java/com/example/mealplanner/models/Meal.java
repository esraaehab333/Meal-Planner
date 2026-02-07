package com.example.mealplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Meal implements Parcelable {

    private String idMeal;
    private String strMeal;
    private String strMealAlternate = null;
    private String strCategory;
    private String strArea;
    private String strInstructions;
    private String strMealThumb;
    private String strTags = null;
    private String strYoutube;

    private String strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
            strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20;

    private String strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20;

    private String strSource;
    private String strImageSource = null;
    private String strCreativeCommonsConfirmed = null;
    private String dateModified = null;

    public Meal() {}

    protected Meal(Parcel in) {
        idMeal = in.readString();
        strMeal = in.readString();
        strMealAlternate = in.readString();
        strCategory = in.readString();
        strArea = in.readString();
        strInstructions = in.readString();
        strMealThumb = in.readString();
        strTags = in.readString();
        strYoutube = in.readString();

        String[] ingredients = in.createStringArray();
        strIngredient1 = ingredients[0];
        strIngredient2 = ingredients[1];
        strIngredient3 = ingredients[2];
        strIngredient4 = ingredients[3];
        strIngredient5 = ingredients[4];
        strIngredient6 = ingredients[5];
        strIngredient7 = ingredients[6];
        strIngredient8 = ingredients[7];
        strIngredient9 = ingredients[8];
        strIngredient10 = ingredients[9];
        strIngredient11 = ingredients[10];
        strIngredient12 = ingredients[11];
        strIngredient13 = ingredients[12];
        strIngredient14 = ingredients[13];
        strIngredient15 = ingredients[14];
        strIngredient16 = ingredients[15];
        strIngredient17 = ingredients[16];
        strIngredient18 = ingredients[17];
        strIngredient19 = ingredients[18];
        strIngredient20 = ingredients[19];

        String[] measures = in.createStringArray();
        strMeasure1 = measures[0];
        strMeasure2 = measures[1];
        strMeasure3 = measures[2];
        strMeasure4 = measures[3];
        strMeasure5 = measures[4];
        strMeasure6 = measures[5];
        strMeasure7 = measures[6];
        strMeasure8 = measures[7];
        strMeasure9 = measures[8];
        strMeasure10 = measures[9];
        strMeasure11 = measures[10];
        strMeasure12 = measures[11];
        strMeasure13 = measures[12];
        strMeasure14 = measures[13];
        strMeasure15 = measures[14];
        strMeasure16 = measures[15];
        strMeasure17 = measures[16];
        strMeasure18 = measures[17];
        strMeasure19 = measures[18];
        strMeasure20 = measures[19];

        strSource = in.readString();
        strImageSource = in.readString();
        strCreativeCommonsConfirmed = in.readString();
        dateModified = in.readString();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMeal);
        dest.writeString(strMeal);
        dest.writeString(strMealAlternate);
        dest.writeString(strCategory);
        dest.writeString(strArea);
        dest.writeString(strInstructions);
        dest.writeString(strMealThumb);
        dest.writeString(strTags);
        dest.writeString(strYoutube);

        dest.writeStringArray(getIngredientsArray());
        dest.writeStringArray(getMeasuresArray());

        dest.writeString(strSource);
        dest.writeString(strImageSource);
        dest.writeString(strCreativeCommonsConfirmed);
        dest.writeString(dateModified);
    }

    public List<String> getIngredientsList() {
        List<String> list = new ArrayList<>();
        String[] arr = getIngredientsArray();
        for (String s : arr) {
            if (s != null && !s.trim().isEmpty())
                list.add(s);
        }
        return list;
    }

    public List<String> getMeasuresList() {
        List<String> list = new ArrayList<>();
        String[] arr = getMeasuresArray();
        for (String s : arr) {
            if (s != null && !s.trim().isEmpty())
                list.add(s);
        }
        return list;
    }

    private String[] getIngredientsArray() {
        return new String[]{strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
                strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20};
    }

    private String[] getMeasuresArray() {
        return new String[]{strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
                strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
                strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
                strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20};
    }

    public String getIdMeal() { return idMeal; }
    public void setIdMeal(String idMeal) { this.idMeal = idMeal; }

    public String getStrMeal() { return strMeal; }
    public void setStrMeal(String strMeal) { this.strMeal = strMeal; }

    public String getStrInstructions() { return strInstructions; }
    public void setStrInstructions(String strInstructions) { this.strInstructions = strInstructions; }

    public String getStrMealThumb() { return strMealThumb; }
    public void setStrMealThumb(String strMealThumb) { this.strMealThumb = strMealThumb; }

    public String getStrCategory() { return strCategory; }
    public void setStrCategory(String strCategory) { this.strCategory = strCategory; }

    public String getStrArea() { return strArea; }
    public void setStrArea(String strArea) { this.strArea = strArea; }

    public String getStrTags() { return strTags; }
    public void setStrTags(String strTags) { this.strTags = strTags; }

    public String getStrYoutube() { return strYoutube; }
    public void setStrYoutube(String strYoutube) { this.strYoutube = strYoutube; }

    public String getStrSource() { return strSource; }
    public void setStrSource(String strSource) { this.strSource = strSource; }

    public String getStrImageSource() { return strImageSource; }
    public void setStrImageSource(String strImageSource) { this.strImageSource = strImageSource; }

    public String getStrCreativeCommonsConfirmed() { return strCreativeCommonsConfirmed; }
    public void setStrCreativeCommonsConfirmed(String strCreativeCommonsConfirmed) { this.strCreativeCommonsConfirmed = strCreativeCommonsConfirmed; }

    public String getDateModified() { return dateModified; }
    public void setDateModified(String dateModified) { this.dateModified = dateModified; }
}
