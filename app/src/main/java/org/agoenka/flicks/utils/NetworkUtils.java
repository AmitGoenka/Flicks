package org.agoenka.flicks.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

/**
 * Author: agoenka
 * Created At: 10/16/2016
 * Version: ${VERSION}
 */

public final class NetworkUtils {

    private NetworkUtils () {}

    private static final String MOVIE_DB_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String YOUTUBE_API_KEY = "AIzaSyAVYQ5xw9-MVozOr5AdmDKgzjjyaBJNzng";

    public static AsyncHttpClient getClient() {
        return new AsyncHttpClient();
    }

    private static String getBaseUrl() {
        return "https://api.themoviedb.org/3/movie/";
    }

    public static String getNowPlayingUrl() {
        return getBaseUrl() + "now_playing";
    }

    public static String getVideosUrl(long movieId) {
        return getBaseUrl() + movieId + "/videos";
    }

    public static RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("api_key", MOVIE_DB_API_KEY);
        return params;
    }

}
