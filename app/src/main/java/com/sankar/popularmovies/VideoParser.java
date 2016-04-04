package com.sankar.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by muthu ganesan on 3/22/2016.
 */
public class VideoParser {

    public ArrayList<Trailer> parseJsonString(String jsonStr) {

        ArrayList<Trailer> trailerList = new ArrayList<>();
        String trailerId;
        String language;
        String countryCode;
        String key;
        String site;
        String name;
        int size;
        String type;
        long movieId;

        //MovieService movieService = MovieService.getInstance();
        //movieService.removeAllMovies();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            movieId = jsonObject.getLong("id");
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trailerObject = (JSONObject) jsonArray.get(i);
                Trailer trailer = new Trailer();
                trailerId = trailerObject.getString("id");
                trailer.setTrailerId(trailerId);
                language = trailerObject.getString("iso_639_1");
                trailer.setLanguage(language);
                countryCode = trailerObject.getString("iso_3166_1");
                trailer.setCountryCode(countryCode);
                key = trailerObject.getString("key");
                trailer.setKey(key);
                site = trailerObject.getString("site");
                trailer.setSite(site);
                name = trailerObject.getString("name");
                trailer.setName(name);
                size = trailerObject.getInt("size");
                trailer.setSize(size);
                type = trailerObject.getString("type");
                trailer.setType(type);
                trailer.setMovieId(movieId);
                trailerList.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trailerList;

    }
}