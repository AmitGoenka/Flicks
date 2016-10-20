package org.agoenka.flicks.network;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author: agoenka
 * Created At: 10/19/2016
 * Version: ${VERSION}
 */

public class MovieDbClient {

    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_RELATIVE_URL_NOW_PLAYING = "now_playing";
    private static final String API_RELATIVE_URL_VIDEOS = "videos";

    private OkHttpClient client;

    public MovieDbClient() {
        this.client = getClient();
    }

    private static OkHttpClient getClient() {
        return new OkHttpClient();
    }

    private void execute(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    private static HttpUrl.Builder getBaseUrl() {
        return HttpUrl.parse(API_BASE_URL)
                .newBuilder()
                .addQueryParameter("api_key", API_KEY);
    }

    private static String getNowPlayingUrl() {
        return getBaseUrl()
                .addPathSegment(API_RELATIVE_URL_NOW_PLAYING)
                .build()
                .toString();
    }

    private static String getVideosUrl(long movieId) {
        return getBaseUrl()
                .addPathSegment(String.valueOf(movieId))
                .addPathSegment(API_RELATIVE_URL_VIDEOS)
                .build()
                .toString();
    }

    public void getNowPlayingMovies(Callback callback) {
        Request request = new Request.Builder()
                .url(getNowPlayingUrl())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getVideos(long movieId, Callback callback) {
        Request request = new Request.Builder()
                .url(getVideosUrl(movieId))
                .build();
        execute(request, callback);
    }

}