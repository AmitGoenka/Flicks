package org.agoenka.flicks.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.agoenka.flicks.R;
import org.agoenka.flicks.models.Movie;
import org.agoenka.flicks.models.Video;
import org.agoenka.flicks.network.MovieDbClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.agoenka.flicks.network.NetworkUtils.YOUTUBE_API_KEY;

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
        new MovieDbClient().getVideos(movieId, new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // check for failure using before proceeding
                if (!response.isSuccessful()) {
                    YoutubePlayerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("DEBUG", response.body().string());
                                Log.d("DEBUG", String.format("Response Status Code: %s", response.code()));
                                Toast.makeText(YoutubePlayerActivity.this, "The request to fetch videos was unsuccessful!", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Log.d("DEBUG", e.getMessage());
                            }
                        }
                    });
                } else {
                    try {
                        // Read data on the worker thread
                        String responseData = response.body().string();
                        // extracting the video results from the json response
                        JSONArray videoJsonResults = new JSONObject(responseData).getJSONArray("results");
                        videos = Video.fromJsonArray(videoJsonResults);
                        video = Video.findVideo(videos, "Trailer", 0);

                        // Run view-related code back on the main thread
                        YoutubePlayerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (video == null) {
                                    Toast.makeText(YoutubePlayerActivity.this, "The requested video is not available!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    initYoutubePlayer();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        Log.d("DEBUG", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                // Run view-related code back on the main thread
                YoutubePlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("DEBUG", e.getMessage());
                        Toast.makeText(YoutubePlayerActivity.this, "Some error occurred while fetching the requested video!", Toast.LENGTH_SHORT).show();
                    }
                });
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
                Toast.makeText(YoutubePlayerActivity.this, "Error occurred while initializing the YouTube player!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}