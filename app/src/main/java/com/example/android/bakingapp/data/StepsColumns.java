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

public interface StepsColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(TEXT) @NotNull String COLUMN_RECIPE_ID = "recipe_id";
    @DataType(TEXT) @NotNull  String COLUMN_SHORT_DESCRIPTION = "short_description";
    @DataType(TEXT) @NotNull  String COLUMN_DESCRIPTION = "description";
    @DataType(TEXT) @NotNull  String COLUMN_VIDEO_URL = "video_url";
    @DataType(TEXT) @NotNull  String COLUMN_THUMBNAIL_URL = "thumbnail_url";
}
