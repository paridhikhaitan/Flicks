package com.example.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flicks.models.Config;
import com.example.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //the base URL for the API
    public final static String API_BASE_URL="https://api.themoviedb.org/3";
    //parameter name for the api_key
    public final static String API_KEY_PARAM="api_key";
    //for logging activity
    public final static String TAG="MainActivity";

    AsyncHttpClient client;

    //base url for loading images
    String imageBaseUrl;
    //the poster size to use when fetching images, part of the url
    String posterSize;

    //the list of currently playing movies
    ArrayList<Movie> movies;

    //Trying to track the recycler view and the adapter
    //the recycler view
    RecyclerView rvMovies;

    //movie adapter
    MovieAdapter adapter;

    //track the Config Object
    Config config;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the client, you will use this whenever you want to communicate with the network
        client= new AsyncHttpClient();

        movies= new ArrayList<>();

        //initialize the adapter- movies array list cannot be modified after it has been linked
        adapter= new MovieAdapter(movies);

        //resolve the recycler view and connect a layout manager and the adapter

        //ties the recycler view (implemented in main_activity.xml
        //ties and sets it to the new MovieAdapter that we just created
        rvMovies= (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get the configuration on app creation
        getConfiguration();

    }
    //get the list of currently playing movies from the API
    private void getNowPlaying(){
        //the same process of constructing the URL

        //create the url where we will be sending our API request to
        //change the endpoint
        String url= API_BASE_URL+"/movie/now_playing";
        //parameters for request
        RequestParams params= new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));//the API key is always always required

        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the results into movies list
                try {
                    JSONArray results= response.getJSONArray("results");
                    //iterate through result set and create Movie objects
                    //pass each object to the movie ctor
                    for(int i=0; i<results.length(); i++){
                        Movie movie= new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify the adapter that the list has changed
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failure while parsing from endpoint now_playing", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from the now_playing endpoint", throwable, true);
            }
        });
    }

    //get the configuration from the API
    private void getConfiguration(){
        //create the url where we will be sending our API request to
        String url= API_BASE_URL+"/configuration";
        //parameters for request
        RequestParams params= new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));//the API key is always always required

        //actually perform a JSON Request
        //the 'url' is the one we constructed from above
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //the super class has all the methods, we're only overwriting some of them
                //like the onSuccess and onFailure methods :)
                //get the image base url

                //we surrounded this with a try catch methos, and in the catch part, just log the
                //error using getLog
                try {
                    config= new Config(response);

                    Log.i(TAG, String.format("Loaded configuration with imageBaseURL %s and posterSize %s", config.getImageBaseUrl(), config.getPosterSize()));
                    //we need to pass the config object to the adapter

                    adapter.setConfig(config);

                    //makes sure that getNowPlaying only gets configured after
                    //get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuraiton", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Get request failed", throwable, true);
            }
        });
    }

    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);
        //alert the user when something goes wrong
        if(alertUser){
            //show the user a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
