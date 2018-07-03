package com.example.android.bakingapp.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kanchan on 26-02-2018.
 */

public class Recipe implements Parcelable{

    private int id;
    private String recipeName;
    private String imageUrl;
    private int servingCount;
    private String markAsFavorite;


    private Recipe(Parcel in) {
        id = in.readInt();
        recipeName = in.readString();
        imageUrl = in.readString();
        servingCount = in.readInt();
        markAsFavorite = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipeName);
        dest.writeString(imageUrl);
        dest.writeInt(servingCount);
        dest.writeString(markAsFavorite);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(){
        super();
    }

    public Recipe(int id, String recipeName, String imageUrl, int servingCount,String markAsFavorite) {
        this.id = id;
        this.recipeName = recipeName;
        this.imageUrl = imageUrl;
        this.servingCount = servingCount;
        this.markAsFavorite = markAsFavorite;
    }

    public String getMarkAsFavorite() {
        return markAsFavorite;
    }

    public void setMarkAsFavorite(String markAsFavorite) {
        this.markAsFavorite = markAsFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getServingCount() {
        return servingCount;
    }

    public void setServingCount(int servingCount) {
        this.servingCount = servingCount;
    }
}
