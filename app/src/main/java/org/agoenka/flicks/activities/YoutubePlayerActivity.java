package org.agoenka.flicks.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.agoenka.flicks.R;
import org.agoenka.flicks.models.Movie;
import org.agoenka.flicks.models.Video;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static org.agoenka.flicks.utils.NetworkUtils.YOUTUBE_API_KEY;
import static org.agoenka.flicks.utils.NetworkUtils.getClient;
import static org.agoenka.flicks.utils.NetworkUtils.getParams;
import static org.agoenka.flicks.utils.NetworkUtils.getVideosUrl;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    List<Video> videos;
    Video video;

    @BindView(R.id.ytPlayer) YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        ButterKnife.bind(this);

        videos = new ArrayList<>();
        video = null;

        if (getIntent() != null) {
            Movie movie = (Movie) getIntent().getSerializableExtra("selectedMovie");
            fetchVideos(movie.getId());
        }
    }

    private void fetchVideos(long movieId) {
        getClient().get(getVideosUrl(movieId), getParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray videoJsonResults;
                try {
                    // extracting the movie results from the json response
                    videoJsonResults = response.getJSONArray("results");
                    videos = Video.fromJsonArray(videoJsonResults);
                    video = Video.findVideo(videos, "Trailer", 0);
                    initYoutubePlayer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void initYoutubePlayer () {
        playerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // cue video, perform video etc.
                if (video != null && video.getKey() != null && video.getKey().length() > 0) {
                    youTubePlayer.loadVideo(video.getKey());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // log failures here
                Log.d("DEBUG", youTubeInitializationResult.toString());
            }
        });
    }
}