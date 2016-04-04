package com.sankar.popularmovies;

/**
 * Created by muthu ganesan on 3/22/2016.
 */
public class Trailer {
    private String trailerId;
    private String language;
    private String countryCode;
    private String key;
    private String site;
    private String name;
    private int size;
    private String type;
    private long movieId;

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "trailerId='" + trailerId + '\'' +
                ", language='" + language + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", key='" + key + '\'' +
                ", site='" + site + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", type='" + type + '\'' +
                ", movieId=" + movieId +
                '}';
    }
}
