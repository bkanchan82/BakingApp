package com.example.android.bakingapp.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by kanchan on 23-02-2018.
 */
@Database(version = RecipeDatabase.VERSION,
        packageName = "com.example.android.bakingapp.data.provider")
public final class RecipeDatabase {
    public static final int VERSION = 1;

    @Table(RecipeColumns.class) public static final String RECIPES = "recipe";
    @Table(IngredientsColumns.class) public static final String INGREDIENTS = "ingredient";
    @Table(StepsColumns.class) public static final String STEPS = "steps";
}
