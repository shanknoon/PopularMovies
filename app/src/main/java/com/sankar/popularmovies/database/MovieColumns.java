package com.sankar.popularmovies.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface MovieColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement
    public static final String ID = "_id";
    @DataType(INTEGER) @Unique
    public static final String MOVIE_ID = "movie_id";
    @DataType(TEXT) @NotNull
    public static final String TITLE = "title";
    @DataType(TEXT)
    public static final String IMAGE_PATH = "image_path";
    @DataType(INTEGER)
    public static final String POPULARITY = "popularity";
    @DataType(INTEGER)
    public static final String VOTE_COUNT = "vote_count";
    @DataType(INTEGER)
    public static final String VOTE_AVERAGE = "vote_average";
    @DataType(TEXT)
    public static final String OVERVIEW = "overview";
    @DataType(TEXT)
    public static final String RELEASE_DATE = "release_date";

}