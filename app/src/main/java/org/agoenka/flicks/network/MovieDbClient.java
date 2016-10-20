package org.agoenka.flicks.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author: agoenka
 * Created At: 10/19/2016
 * Version: ${VERSION}
 */

public class MovieDbClient {

    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_RELATIVE_URL_NOW_PLAYING = "now_playing";
    private static final String API_RELATIVE_URL_VIDEOS = "/videos";

    private AsyncHttpClient client;

    public MovieDbClient () {
        this.client = getClient();
    }

    private static AsyncHttpClient getClient() {
        return new AsyncHttpClient();
    }

    private static String getNowPlayingUrl() {
        return API_BASE_URL + API_RELATIVE_URL_NOW_PLAYING;
    }

    private static String getVideosUrl(long movieId) {
        return API_BASE_URL + movieId + API_RELATIVE_URL_VIDEOS;
    }

    private static RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);
        return params;
    }

    public void getNowPlayingMovies(JsonHttpResponseHandler handler) {
        String url = getNowPlayingUrl();
        RequestParams params = getParams();
        client.get(url, params, handler);
    }

    public void getVideos(long movieId, JsonHttpResponseHandler handler) {
        String url = getVideosUrl(movieId);
        RequestParams params = getParams();
        client.get(url, params, handler);
    }

}