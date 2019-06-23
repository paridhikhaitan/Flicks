package com.example.flicks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flicks.models.Config;
import com.example.flicks.models.Movie;
import com.example.flicks.models.MovieDetailsActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    //store the list of movies in this ArrayList
    ArrayList<Movie> movies;

    //pass the config class to the adapter
    Config config;

    Context context;

    String backgroundURL;

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    //create the ViewHolder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //track the view objects: namely the two text views and image view
        ImageView ivPosterImage;
        TextView movieTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage= (ImageView)itemView.findViewById(R.id.ivPosterImage);
            ivPosterImage= (ImageView)itemView.findViewById(R.id.ivPosterImage);
            tvOverview= (TextView)itemView.findViewById(R.id.tvOverview);
            movieTitle= (TextView)itemView.findViewById(R.id.movieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position= getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                Movie movie= movies.get(position);

                backgroundURL= config.getImageURL(config.getBackdropSize(), movie.getBackdropPath());

                Intent intent= new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                intent.putExtra("backgroundURL",backgroundURL);
                context.startActivity(intent);
                Toast.makeText(context, movieTitle.getText(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    //these are methods that HAVE to be implemented

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context= viewGroup.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        //we pass the ID=> item_movie that we want to inflate using the inflater
        View movieView= inflater.inflate(R.layout.item_movie, viewGroup, false);
        //return a new ViewHolder

        return new ViewHolder(movieView);
    }

    //associates the inflated view (in our case the item_movie) to a specific item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get the movie data in the specific position=> second param
        Movie movie= movies.get(i);
        //populate the view with the movie data
        //the view is what got passed from the onCreateViewHolder()

        //here basically, based on the viewholder that was created, you
        //are populating the movieTitle, tvOverview tags in the design
        //with the data from the Movie class and constructor
        viewHolder.movieTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //build the URL for the poster image using the Config object
        String imageURL= config.getImageURL(config.getPosterSize(), movie.getPosterPath());

        //load image using Glide
        Glide.with(context)
                .load(imageURL)
                .bitmapTransform(new RoundedCornersTransformation(context, 50, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(viewHolder.ivPosterImage);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

}
