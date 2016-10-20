package org.agoenka.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.agoenka.flicks.R;
import org.agoenka.flicks.models.Movie;
import org.agoenka.flicks.models.Video;
import org.agoenka.flicks.network.MovieDbClient;
import org.agoenka.flicks.utils.PicassoUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private Picasso picasso;
    Movie movie;
    Video video;

    @BindView(R.id.tvOriginalTitle) TextView tvOriginalTitle;
    @BindView(R.id.tvSynopsis) TextView tvSynopsis;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.tvPopularity) TextView tvPopularity;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
    @BindView(R.id.ivPlayerIcon) ImageView ivPlayerIcon;
    @BindView(R.id.layoutImage) FrameLayout layoutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            movie = (Movie) getIntent().getSerializableExtra("selectedMovie");
        }

        if (movie != null) {
            tvOriginalTitle.setText(movie.getOriginalTitle());
            tvSynopsis.setText(movie.getOverview());
            tvReleaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
            tvPopularity.setText(String.format("Popularity: %s", movie.getPopularity()));
            ratingBar.setRating((float) movie.getVoteAverage());

            if (picasso == null) picasso = PicassoUtils.newInstance(this);
            if (PicassoUtils.isValidImagePath(movie.getBackdropPath())) {
                picasso.load(movie.getBackdropPath("w780"))
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.mipmap.ic_launcher)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .into(ivBackdropImage);
            } else {
                picasso.load(R.mipmap.ic_launcher)
                        .into(ivBackdropImage);
            }

            fetchVideos(movie.getId());
        }
    }

    private void fetchVideos(long movieId) {
        new MovieDbClient().getVideos(movieId, new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // check for failure using before proceeding
                if (!response.isSuccessful()) {
                    Log.d("DEBUG", response.body().string());
                    Log.d("DEBUG", String.format("Response Status Code: %s", response.code()));
                } else {
                    // Read data on the worker thread
                    video = MovieDbClient.getVideo(response);

                    if (video != null) {
                        setOnClickListener();
                    }
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d("DEBUG", e.getMessage());
            }
        });
    }

    public void setOnClickListener() {
        // Run view-related code back on the main thread
        MovieDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivPlayerIcon.setVisibility(View.VISIBLE);
                layoutImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MovieDetailActivity.this, YoutubePlayerActivity.class);
                        intent.putExtra("selectedVideo", video);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}