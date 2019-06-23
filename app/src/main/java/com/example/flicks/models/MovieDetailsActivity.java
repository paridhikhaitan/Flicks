package com.example.flicks.models;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flicks.R;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView movieBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the current view of the app to this layout xml file
        setContentView(R.layout.activity_movie_details2);
        // resolve the view objects


        //helps find the specific UI elements
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvDetails);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        movieBackdrop= (ImageView) findViewById(R.id.movie_backdrop);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        String backdropSize = getIntent().getStringExtra("backdrop_size");

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        String backdropUrl= getIntent().getStringExtra("backgroundURL");

        Glide.with(this).load(backdropUrl).into(this.movieBackdrop);
/*
        //build the URL for the poster image using the Config object
        String imageURL= config.getImageURL(config.getBackdropSize(), movie.getBackdropPath());

        //load image using Glide
        Glide.with(this)
                .load(imageURL)
                .bitmapTransform(new RoundedCornersTransformation(this, 50, 0))
                .into(this.movieBackdrop);*/


        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }
}