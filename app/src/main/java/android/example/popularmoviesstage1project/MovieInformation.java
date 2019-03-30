package android.example.popularmoviesstage1project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieInformation extends AppCompatActivity {

    private TextView title, releaseDate, voteAverage, plotSynposis;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        title = findViewById(R.id.textView2);
        releaseDate = findViewById(R.id.textView4);
        voteAverage = findViewById(R.id.textView6);
        plotSynposis = findViewById(R.id.textView8);

        imageView = findViewById(R.id.imageView);


        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        Picasso.get()
                .load(movie.getMoviePoster())
                .into(imageView);

        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(movie.getVoteAverage());
        plotSynposis.setText(movie.getPlotSynopsis());

    }
}
