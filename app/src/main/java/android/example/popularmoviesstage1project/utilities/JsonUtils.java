package android.example.popularmoviesstage1project.utilities;

import android.example.popularmoviesstage1project.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String MOVIE_TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String PLOT_SYNOPSIS = "overview";
    private static final String USER_RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";


    public static ArrayList<Movie> parseMovieJson(String json) {
        ArrayList<Movie> movieList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++ ){

                JSONObject movieInfo = jsonArray.getJSONObject(i);

                String movieTitle = movieInfo.getString(MOVIE_TITLE);
                String movieReleaseDate = movieInfo.getString(RELEASE_DATE);
                String moviePoster = IMAGE_URL + movieInfo.getString(POSTER_PATH);
                String movieVoteAverage = movieInfo.getString(USER_RATING);
                String moviePlotSynopsis = movieInfo.getString(PLOT_SYNOPSIS);

                movieList.add(new Movie(movieTitle, movieReleaseDate, moviePoster, movieVoteAverage, moviePlotSynopsis));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;

    }

}
