package org.agoenka.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.agoenka.flicks.R;
import org.agoenka.flicks.activities.YoutubePlayerActivity;
import org.agoenka.flicks.models.Movie;
import org.agoenka.flicks.utils.PicassoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static org.agoenka.flicks.models.Movie.isPopular;

/**
 * Author: agoenka
 * Created At: 10/15/2016
 * Version: ${VERSION}
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private Picasso picasso;

    private static abstract class ViewHolder {}

    static class ViewHolderNormal extends ViewHolder {
        @BindView(R.id.ivBackdropImage) ImageView ivImage;
        @BindView(R.id.tvOriginalTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        ViewHolderNormal (View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderPopular extends ViewHolder {
        @BindView(R.id.ivBackdropImage) ImageView ivImage;
        @BindView(R.id.ivPlayerIcon) ImageView ivPlayerIcon;

        ViewHolderPopular(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return 2;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        // Return an integer here representing the type of View.
        // Note: Integers must be in the range 0 to getViewTypeCount() - 1
        Movie item = getItem(position);
        if (!isPopular(item)) {
            return 0;
        } else {
            return 1;
        }
    }

    // Get a View that displays the data at the specified position in the data set.
    // View should be created based on the type returned from `getItemViewType(int position)`
    // convertView is guaranteed to be the "correct" recycled type
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        int viewType = getItemViewType(position);

        // Get the data item for this position
        Movie movie = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = getInflatedLayoutForType(viewType, parent);
            ViewHolder viewHolder = initViewHolderForType(viewType, convertView);
            convertView.setTag(viewHolder);
        }

        if (viewType == 0) updateViewForNormalMovie(convertView, movie);
        if (viewType == 1) updateViewForPopularMovie(convertView, movie);

        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(int type, ViewGroup parent) {
        switch (type) {
            case 0: return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_normal, parent, false);
            case 1: return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, parent, false);
            default: return null;
        }
    }

    // Given the item type and view, responsible for returning the correct View Holder instance
    private ViewHolder initViewHolderForType(int type, View convertView) {
        switch (type) {
            case 0: return new ViewHolderNormal(convertView);
            case 1: return new ViewHolderPopular(convertView);
            default: return null;
        }
    }

    private void updateViewForNormalMovie (View convertView, Movie movie) {
        ViewHolderNormal viewHolder = (ViewHolderNormal) convertView.getTag();

        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        if (picasso == null) picasso = PicassoUtils.newInstance(getContext());
        if (PicassoUtils.isValidImagePath(movie.getPosterPath())) {
            picasso.load(movie.getPosterPath("w500"))
                    .placeholder(R.drawable.placeholder_large)
                    .error(R.mipmap.ic_launcher)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolder.ivImage);
        } else {
            picasso.cancelRequest(viewHolder.ivImage);
            viewHolder.ivImage.setImageDrawable(null);
        }
    }

    private void updateViewForPopularMovie (View convertView, Movie movie) {
        ViewHolderPopular viewHolder = (ViewHolderPopular) convertView.getTag();

        if (picasso == null) picasso = PicassoUtils.newInstance(getContext());
        if (PicassoUtils.isValidImagePath(movie.getBackdropPath())) {
            picasso.load(movie.getBackdropPath("w780"))
                    .placeholder(R.drawable.placeholder_large)
                    .error(R.mipmap.ic_launcher)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .into(viewHolder.ivImage);
            initPlayerIcon(viewHolder, movie);
        } else {
            picasso.load(R.mipmap.ic_launcher)
                    .into(viewHolder.ivImage);
        }
    }

    private void initPlayerIcon(ViewHolderPopular viewHolder, Movie movie) {
        viewHolder.ivPlayerIcon.setTag(movie);
        viewHolder.ivPlayerIcon.setVisibility(View.VISIBLE);
        viewHolder.ivPlayerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = (Movie) v.getTag();
                Intent intent = new Intent(getContext(), YoutubePlayerActivity.class);
                intent.putExtra("selectedMovie", movie);
                getContext().startActivity(intent);
            }
        });
    }

}