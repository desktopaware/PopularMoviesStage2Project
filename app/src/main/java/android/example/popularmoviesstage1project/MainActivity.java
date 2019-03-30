package android.example.popularmoviesstage1project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import java.net.URL;
import java.util.List;

import static android.example.popularmoviesstage1project.utilities.JsonUtils.parseMovieJson;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.buildUrl;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.getResponseFromHttpUrl;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private String requestData;
    private TextView connectionError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(movieAdapter);
        requestData = "top_rated";
        connectionError = findViewById(R.id.connection_error);


        if (isOnline()) {
            fetchData();
        }else {
            connectionError.setVisibility(View.VISIBLE);
        }
    }

    private void fetchData() {
        String queryType = requestData;
        new MovieAsyncTask().execute(queryType);
    }

    //You must make sure your app does not crash when there is no network connection!
    // You can see this StackOverflow post on how to do this.
    //https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    //is link provide by udacity
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
                movieAdapter = new MovieAdapter(getApplicationContext(), MovieResults);
                recyclerView.setAdapter(movieAdapter);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


