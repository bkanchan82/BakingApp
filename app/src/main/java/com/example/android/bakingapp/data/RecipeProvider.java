package com.example.android.bakingapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by kanchan on 23-02-2018.
 */
@ContentProvider(authority = RecipeProvider.AUTHORITY,database = RecipeDatabase.class,
        packageName = "com.example.android.bakingapp.data.provider")
public final class RecipeProvider {

    public static final String AUTHORITY = "com.example.android.bakingapp.provider";

    @TableEndpoint(table = RecipeDatabase.RECIPES) public static class Recipes{
        @ContentUri(
                path = "recipe",
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeColumns._ID + " ASC")
        public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/recipe");
    }

    @InexactContentUri(
            path = "recipe" + "/#",
            name = "RECIPE_ID",
            type = "vnd.android.cursor.item/recipe",
            whereColumn = RecipeColumns._ID,
            pathSegment = 1)
    public static Uri withId(long id){
        return Uri.parse("content://" + AUTHORITY + "/recipe/" + id);
    }

    @TableEndpoint(table = RecipeDatabase.INGREDIENTS)
    public static class Ingredients {
        @ContentUri(
                path = "ingredient",
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = RecipeColumns._ID + " ASC")
        public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/ingredient");
    }

    @InexactContentUri(
            path = "ingredient" + "/#",
            name = "INGREDIENT_ID",
            type = "vnd.android.cursor.item/ingredient",
            whereColumn = RecipeColumns._ID,
            pathSegment = 1)
    public static Uri ingredientWithId(long id) {
        return Uri.parse("content://" + AUTHORITY + "/ingredient/" + id);
    }



    @TableEndpoint(table = RecipeDatabase.STEPS)
    public static class Steps {
        @ContentUri(
                path = "steps",
                type = "vnd.android.cursor.dir/steps",
                defaultSort = RecipeColumns._ID + " ASC")
        public static final Uri URI = Uri.parse("content://" + AUTHORITY + "/steps");
    }

    @InexactContentUri(
            path = "steps" + "/#",
            name = "STEPS_ID",
            type = "vnd.android.cursor.item/steps",
            whereColumn = StepsColumns._ID,
            pathSegment = 1)
    public static Uri stepsWithId(long id) {
        return Uri.parse("content://" + AUTHORITY + "/steps/" + id);
    }

}
