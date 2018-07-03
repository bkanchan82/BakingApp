package com.example.android.bakingapp.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kanchan on 28-02-2018.
 */

public class RecipeStep implements Parcelable {

    private int id;
    private String recipeId;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof RecipeStep){
            RecipeStep objRecipeStep = (RecipeStep)obj;

            if(id == objRecipeStep.getId()){
                return true;
            }
        }

        return super.equals(obj);
    }

    private RecipeStep(Parcel in) {
        id = in.readInt();
        recipeId = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(recipeId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };

    public RecipeStep(){
        super();
    }

    public RecipeStep(int id, String recipeId, String shortDescription, String description, String videoUrl,String thumbnailUrl) {
        this.id = id;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    private int getId() {
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
