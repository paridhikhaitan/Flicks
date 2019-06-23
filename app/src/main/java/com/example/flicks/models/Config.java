package com.example.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //base url for loading images
    String imageBaseUrl;
    //the poster size to use when fetching images, part of the url
    String posterSize;

    String backdropUrl;

    String backdropSize;


    public Config(JSONObject object) throws JSONException {

        //Consturction of the Image Base URL
        JSONObject images= object.getJSONObject("images");
        imageBaseUrl= images.getString("secure_base_url");
        //get the whole poster_size array in a string, and then get an index
        JSONArray posterSizeOptions= images.getJSONArray("poster_sizes");
        JSONArray backdropSizeOptions= images.getJSONArray("backdrop_sizes");

        backdropSize= backdropSizeOptions.optString(3, "w1280");

        //use the option at the 3 index
        //pass a fallback incase it cannot
        posterSize= posterSizeOptions.optString(3,"w342" );
    }

    //helper method to contruct the URL for getting the poster image
    public String getImageURL(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path); //concatenate
    }

    public String getBackdropSize() {
        return backdropSize;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
