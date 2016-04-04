package com.sankar.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.sankar.popularmovies.Movie;

import java.util.HashMap;
import java.util.Map;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;
//import com.sankar.popularmovies.database.MovieDatabase.Tables;

@ContentProvider(authority = MovieProvider.AUTHORITY,
        database = MovieDatabase.class)
public final class MovieProvider {

    private MovieProvider() {
    }

    public static final String AUTHORITY = "com.sankar.popularmovies.database.MovieProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        //String LISTS = "lists";
        String MOVIES = "movies";
        //String FROM_LIST = "fromList";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.MOVIES) public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
        defaultSort = MovieColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, String.valueOf(id));
        }
//
//        @InexactContentUri(
//                name = "NOTES_FROM_LIST",
//                path = Path.MOVIE + "/" + Path.FROM_LIST + "/#",
//                type = "vnd.android.cursor.dir/list",
//                whereColumn = NoteColumns.LIST_ID,
//                pathSegment = 2)
//        public static Uri fromList(long listId) {
//            return buildUri(Path.NOTES, Path.FROM_LIST, String.valueOf(listId));
//        }
//
//        @NotifyInsert(paths = Path.MOVIE) public static Uri[] onInsert(ContentValues values) {
//            final long listId = values.getAsLong(NoteColumns.LIST_ID);
//            return new Uri[] {
//                    Lists.withId(listId), fromList(listId),
//            };
//        }
//
//        @NotifyBulkInsert(paths = Path.NOTES)
//        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
//            return new Uri[] {
//                    uri,
//            };
//        }
//
//        @NotifyUpdate(paths = Path.NOTES + "/#") public static Uri[] onUpdate(Context context,
//                                                                              Uri uri, String where, String[] whereArgs) {
//            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
//            Cursor c = context.getContentResolver().query(uri, new String[] {
//                    NoteColumns.LIST_ID,
//            }, null, null, null);
//            c.moveToFirst();
//            final long listId = c.getLong(c.getColumnIndex(NoteColumns.LIST_ID));
//            c.close();
//
//            return new Uri[] {
//                    withId(noteId), fromList(listId), Lists.withId(listId),
//            };
//        }
//
//        @NotifyDelete(paths = Path.NOTES + "/#") public static Uri[] onDelete(Context context,
//                                                                              Uri uri) {
//            final long noteId = Long.valueOf(uri.getPathSegments().get(1));
//            Cursor c = context.getContentResolver().query(uri, null, null, null, null);
//            c.moveToFirst();
//            final long listId = c.getLong(c.getColumnIndex(NoteColumns.LIST_ID));
//            c.close();
//
//            return new Uri[] {
//                    withId(noteId), fromList(listId), Lists.withId(listId),
//            };
//        }
    }
}