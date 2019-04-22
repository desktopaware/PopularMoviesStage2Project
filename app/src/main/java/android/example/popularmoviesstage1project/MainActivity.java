package android.example.popularmoviesstage1project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.example.popularmoviesstage1project.utilities.JsonUtils.parseMovieJson;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.buildUrl;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.getResponseFromHttpUrl;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private String requestData;
    private TextView connectionError;
    private ArrayList<Movie> movieResults;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null && savedInstanceState.containsKey("key")) {
            movieResults = savedInstanceState.getParcelableArrayList("key");

        }else{
            movieResults = new ArrayList<>();
            if (isOnline()) {
                requestData = "top_rated";
                fetchData();
            }else {
                connectionError.setVisibility(View.VISIBLE);
            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(this, movieResults);
        recyclerView.setAdapter(movieAdapter);
        requestData = "top_rated";
        connectionError = findViewById(R.id.connection_error);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieResults.addAll(movies);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", movieResults);
        super.onSaveInstanceState(outState);
    }

    private void fetchData() {
        String queryType = requestData;
        new MovieAsyncTask().execute(queryType);
    }

    //You must make sure your app does not crash when there is no network connection!
    // You can see this StackOverflow post on how to do this.
    //https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    //is link provided by udacity
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class MovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {


        @Override
        protected List<Movie> doInBackground(String... params) {
            if(params.length == 0 ){
                return null;
            }
            String orderBy = params[0];
            URL url = buildUrl(orderBy);

            try {

                String getResponse = getResponseFromHttpUrl(url);

                return parseMovieJson(getResponse);

            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(List<Movie> MovieResults) {
            if(MovieResults != null && !MovieResults.isEmpty()) {
                movieResults.clear();
                movieResults.addAll(MovieResults);
                movieAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.top_rated_movies_item:
                requestData = "top_rated";
                fetchData();
                return true;
            case R.id.popular_movies_item:
                requestData = "popular";
                fetchData();
                return true;
            case R.id.favorite_movies_item:
                viewModel.getFavMovies().observe(this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        movieResults.clear();
                        movieResults.addAll(movies);
                        movieAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


