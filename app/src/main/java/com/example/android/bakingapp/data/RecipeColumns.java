package com.example.android.bakingapp.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by kanchan on 23-02-2018.
 */

public interface RecipeColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(TEXT) @NotNull String COLUMN_RECIPE_NAME = "recipe_name";
    @DataType(TEXT) String COLUMN_IMAGE_URL = "recipe_image_url";
    @DataType(INTEGER) @NotNull String COLUMN_SERVING_COUNT = "serving_id";
    @DataType(INTEGER) @NotNull String COLUMN_MARK_FAVORITE = "mark_as_favorite";

}
