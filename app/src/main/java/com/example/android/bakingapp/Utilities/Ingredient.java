package com.example.android.bakingapp.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kanchan on 28-02-2018.
 */

public class Ingredient implements Parcelable {
    private int id;
    private String recipeId;
    private int quantity;
    private String measuringUnit;
    private String ingredient;

    private Ingredient(Parcel in) {
        id = in.readInt();
        recipeId = in.readString();
        quantity = in.readInt();
        measuringUnit = in.readString();
        ingredient = in.readString();
        
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipeId);
        dest.writeInt(quantity);
        dest.writeString(measuringUnit);
        dest.writeString(ingredient);
        
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Ingredient(){
        super();
    }

    public Ingredient(int id, String recipeId, int quantity, String measuringUnit, String ingredient) {
        this.id = id;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measuringUnit = measuringUnit;
        this.ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
