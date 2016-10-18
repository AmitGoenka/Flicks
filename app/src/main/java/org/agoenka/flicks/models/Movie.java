package org.agoenka.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: agoenka
 * Created At: 10/15/2016
 * Version: ${VERSION}
 */

public class Movie implements Serializable {

    private long id;
    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private long popularity;

    private Movie (JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.popularity = Math.round(jsonObject.getDouble("popularity"));
    }

    public static List<Movie> fromJsonArray(JSONArray array) {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                movies.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    public static boolean isPopular (Movie movie) {
        return movie != null && movie.getVoteAverage() >= 5.0;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPosterPath(String size) {
        return String.format("https://image.tmdb.org/t/p/%s/%s", size, getPosterPath());
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getBackdropPath(String size) {
        return String.format("https://image.tmdb.org/t/p/%s/%s", size, getBackdropPath());
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPopularity() {
        return String.format("%s%s", popularity, "%");
    }

    public long getId() {
        return id;
    }
}