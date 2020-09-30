package org.kazemicode.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import org.kazemicode.flixter.MainActivity;
import org.parceler.Parcel;

import android.util.Log;
@Parcel
public class Movie {
    private String posterPath;
    private String title;
    private String overview;
    private String backdropPath;
    private double rating;
    private String id;

    // emmpty constructor needed by Parceler library
    public Movie(){ }

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        id = jsonObject.getString("id");
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    /* Getters */
    public String getPosterPath()  {
       return  MainActivity.baseUrl + MainActivity.posterSize + posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath(){
        return MainActivity.baseUrl + MainActivity.posterSize + backdropPath;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }
}
