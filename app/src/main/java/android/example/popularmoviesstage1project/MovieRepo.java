package android.example.popularmoviesstage1project;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieRepo {

    private MovieDao movieDao;
    private LiveData<List<Movie>> favMovies;

    public MovieRepo(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        movieDao = appDatabase.movieDao();
        favMovies = movieDao.loadAllFav();
    }

    public LiveData<List<Movie>> getFavMovies(){
        return favMovies;
    }

    public void insert(Movie movie){
        new InsertFavMovieAsyncTask(movieDao).execute(movie);

    }

    public void delete(Movie movie){
        new DeleteFavMovieAsyncTask(movieDao).execute(movie);

    }

    private static class InsertFavMovieAsyncTask extends AsyncTask<Movie, Void, Void>{
        private MovieDao movieDao;

        private InsertFavMovieAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.addToFav(movies[0]);
            return null;
        }
    }

    private static class DeleteFavMovieAsyncTask extends AsyncTask<Movie, Void, Void>{
        private MovieDao movieDao;

        private DeleteFavMovieAsyncTask(MovieDao movieDao){
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.removeFav(movies[0]);
            return null;
        }
    }

}
