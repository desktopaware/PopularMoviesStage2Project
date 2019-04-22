package android.example.popularmoviesstage1project;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.example.popularmoviesstage1project.utilities.JsonUtils.parseReviewJson;
import static android.example.popularmoviesstage1project.utilities.JsonUtils.parseTrailerJson;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.buildUrlReviews;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.buildUrlTrailer;
import static android.example.popularmoviesstage1project.utilities.NetworkUtils.getResponseFromHttpUrl;

public class MovieInformation extends AppCompatActivity implements TrailerAdapter.OnTrailerListener {

    private TextView connectionError;
    private TextView title, releaseDate, voteAverage, plotSynposis;
    private ImageView imageView;
    private TrailerAdapter trailerAdapter;
    private RecyclerView recyclerView;
    private ArrayList<String> movieTrailer;
    private ArrayList<Review> reviewArrayList;
    private int movieId;
    private ImageView star;
    private Button reviewButton;
    private ViewModel viewModel;
    private Boolean ifClicked = false;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        connectionError = findViewById(R.id.textView9);

        if (!isOnline()) {
            connectionError.setVisibility(View.VISIBLE);
        }

        movieTrailer = new ArrayList<>();
        reviewArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.trailer_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailerAdapter = new TrailerAdapter(movieTrailer, this, this);
        recyclerView.setAdapter(trailerAdapter);

        star = findViewById(R.id.star_off);
        title = findViewById(R.id.textView2);
        releaseDate = findViewById(R.id.textView4);
        voteAverage = findViewById(R.id.textView6);
        plotSynposis = findViewById(R.id.textView8);
        imageView = findViewById(R.id.imageView);
        reviewButton = findViewById(R.id.reviews_button);

        movie = getIntent().getParcelableExtra("movie");

        movieId = movie.getId();

        Picasso.get()
                .load(movie.getMoviePoster())
                .into(imageView);

        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(movie.getVoteAverage());
        plotSynposis.setText(movie.getPlotSynopsis());

        new TrailerAsync().execute(String.valueOf(movieId));
        new ReviewAsync().execute(String.valueOf(movieId));

        new SingleMovieAsyncTask().execute(movie);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SingleMovieAsyncTask().execute(movie);
                ifClicked=true;

                Toast.makeText(MovieInformation.this, "Movie added to favorite list!", Toast.LENGTH_SHORT).show();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putParcelableArrayListExtra("review", reviewArrayList);
                startActivity(intent);
            }
        });

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
    @Override
    public void onTrailerClick(int position) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieTrailer.get(position)));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + movieTrailer.get(position)));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

    public class TrailerAsync extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... strings) {
            if(strings.length == 0 ){
                return null;
            }
            String movieId = strings[0];
            URL url = buildUrlTrailer(movieId);

            try {

                String getResponse = getResponseFromHttpUrl(url);

                return parseTrailerJson(getResponse);

            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<String> trailers) {
            if(trailers != null && !trailers.isEmpty()) {
                movieTrailer.clear();
                movieTrailer.addAll(trailers);
                trailerAdapter.notifyDataSetChanged();
            }
        }
    }
    public class ReviewAsync extends AsyncTask<String, Void, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(String... strings) {
            if(strings.length == 0 ){
                return null;
            }
            String movieId = strings[0];
            URL url = buildUrlReviews(movieId);

            try {

                String getResponse = getResponseFromHttpUrl(url);

                return parseReviewJson(getResponse);

            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            if(reviews != null && !reviews.isEmpty()) {
                reviewArrayList.clear();
                reviewArrayList.addAll(reviews);
            }
        }
    }

    private void markAsFavorite(Movie movie) {
        viewModel.insert(movie);
    }

    private void unfavoriteMovie(Movie movie) {
        viewModel.delete(movie);
    }


    private class SingleMovieAsyncTask extends AsyncTask<Movie, Void, Movie> {

        @Override
        protected Movie doInBackground(Movie... movie) {

            AppDatabase appDatabase = AppDatabase.getInstance(MovieInformation.this);
            Movie singleMovie = appDatabase.movieDao().getSingleMovie(movie[0].getId());


            return singleMovie;
        }

        @Override
        protected void onPostExecute(Movie list) {
            super.onPostExecute(list);
            if(ifClicked){
                if (list != null) {
                    unfavoriteMovie(movie);
                    star.setImageResource(R.drawable.unfav_star);

                } else {
                    markAsFavorite(movie);
                    star.setImageResource(R.drawable.fav_star);
                }
            }
            else {
                if (list != null) {
                    star.setImageResource(R.drawable.fav_star);
                } else {
                    star.setImageResource(R.drawable.unfav_star);
                }
            }
        }
    }

}
