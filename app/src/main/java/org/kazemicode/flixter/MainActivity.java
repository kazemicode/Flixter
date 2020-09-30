package org.kazemicode.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kazemicode.flixter.adapters.MovieAdapter;
import org.kazemicode.flixter.models.Movie;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    public static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String NOW_PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/now_playing?api_key=%s",  API_KEY);
    public static final String CONFIGURATION_URL = String.format("https://api.themoviedb.org/3/configuration?api_key=%s", API_KEY);
    public static final String TAG = "MAIN_ACTIVITY";
    public static String baseUrl;
    public static String posterSize;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        // Set the adapter on the RecycleView
        rvMovies.setAdapter(movieAdapter);
        // Set a Layout Manager on the Recycler View
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        final AsyncHttpClient client = new AsyncHttpClient();

        // Get base URL and image size
        client.get(CONFIGURATION_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject images = jsonObject.getJSONObject("images");
                    baseUrl = images.getString("secure_base_url");
                    JSONArray arr = images.getJSONArray("poster_sizes");
                    posterSize = arr.getString(3);

                    // Get Movies
                    client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess");
                            JSONObject jsonObject = json.jsonObject;
                            try {
                                JSONArray results = jsonObject.getJSONArray("results");
                                Log.i(TAG, "Results: " + results.toString());
                                movies.addAll(Movie.fromJsonArray(results));
                                // Let adapter know when movies updates to re-render
                                movieAdapter.notifyDataSetChanged();
                                Log.i(TAG, "Movies: " + movies.get(0).getPosterPath());
                            } catch (JSONException e) {
                                Log.e(TAG, "Hit json exception", e);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.d(TAG, "onFailure");

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Images: " + baseUrl + posterSize);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });






    }
}