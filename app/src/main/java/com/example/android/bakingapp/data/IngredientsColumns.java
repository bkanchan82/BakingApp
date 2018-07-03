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

public interface IngredientsColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement  String _ID = "_id";
    @DataType(TEXT) @NotNull  String COLUMN_RECIPE_ID = "recipe_id";
    @DataType(INTEGER) @NotNull  String COLUMN_QUANTITY = "quantity";
    @DataType(TEXT) @NotNull  String COLUMN_MEASURING_UNIT = "measuring_unit";
    @DataType(TEXT) @NotNull  String COLUMN_INGREDIENT = "ingredient";

}
