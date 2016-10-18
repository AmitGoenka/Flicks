package org.agoenka.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.agoenka.flicks.R;
import org.agoenka.flicks.models.Movie;
import org.agoenka.flicks.utils.PicassoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    private Picasso picasso;
    Movie movie;

    @BindView(R.id.tvOriginalTitle) TextView tvOriginalTitle;
    @BindView(R.id.tvSynopsis) TextView tvSynopsis;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.tvPopularity) TextView tvPopularity;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;

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
            ivBackdropImage.setImageResource(0);

            if (PicassoUtils.isValidImagePath(movie.getBackdropPath())) {
                if (picasso == null) picasso = PicassoUtils.newInstance(this);
                picasso.load(movie.getBackdropPath("w780"))
                        .placeholder(R.drawable.placeholder_large)
                        .error(R.mipmap.ic_launcher)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .into(ivBackdropImage);
            }
        }
    }

    @OnClick(R.id.layoutImage)
    public void OnImageClick(View view) {
        Intent intent = new Intent(this, YoutubePlayerActivity.class);
        intent.putExtra("selectedMovie", movie);
        startActivity(intent);
    }
}