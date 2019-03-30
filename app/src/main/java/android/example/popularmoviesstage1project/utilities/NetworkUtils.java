package android.example.popularmoviesstage1project.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    final static String PARAMETER_API_KEY = "api_key";
    final static String API_KEY = "ea5bbf142b5dfbf441434353f2677fbb";
    final static String PARAMETER_LANGUAGE = "language";
    final static String language = "en-US";

    public static URL buildUrl(String orderBy){
        Uri uri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(orderBy)
                .appendQueryParameter(PARAMETER_API_KEY, API_KEY)
                .appendQueryParameter(PARAMETER_LANGUAGE, language)
                .build();

        URL url = null;
        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
