package org.agoenka.flicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.agoenka.flicks.R;
import org.agoenka.flicks.adapters.MovieArrayAdapter;
import org.agoenka.flicks.models.Movie;
import org.agoenka.flicks.network.MovieDbClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.agoenka.flicks.utils.OsUtils.isNetworkAvailable;

public class MovieActivity extends AppCompatActivity {

    List<Movie> movies;
    MovieArrayAdapter movieAdapter;

    @BindView(R.id.lvMovies) ListView lvItems;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        initSwipeContainer();

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        fetchMovies();
    }

    private void initSwipeContainer() {
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // code to refresh the list.
                // once the network request has completed successfully swipeContainer.setRefreshing(false) must be called.
                fetchMovies();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @OnItemClick(R.id.lvMovies)
    public void OnItemClick(int position) {
        Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
        intent.putExtra("selectedMovie", movies.get(position));
        startActivity(intent);
    }

    private void fetchMovies() {
        if (!isNetworkAvailable(MovieActivity.this)) {
            Toast.makeText(MovieActivity.this, "Internet connectivity is not available. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        } else {
            new MovieDbClient().getNowPlayingMovies(new Callback() {
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        MovieActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d("DEBUG", response.body().string());
                                    Log.d("DEBUG", String.format("Response Status Code: %s", response.code()));
                                    Toast.makeText(MovieActivity.this, "The request to fetch movies was unsuccessful!", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    Log.d("DEBUG", e.getMessage());
                                }
                            }
                        });
                    } else {
                        try {
                            // Read data on the worker thread
                            String responseData = response.body().string();
                            // extracting the movie results from the json response
                            final JSONArray movieJsonResults = new JSONObject(responseData).getJSONArray("results");

                            MovieActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    movieAdapter.clear();
                                    movies.clear();
                                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                                    movieAdapter.notifyDataSetChanged();
                                    Log.d("DEBUG", movies.toString());
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
                    MovieActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DEBUG", e.getMessage());
                            Toast.makeText(MovieActivity.this, "Error occurred while retrieving the movies.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

}