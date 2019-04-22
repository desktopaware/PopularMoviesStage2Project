package android.example.popularmoviesstage1project;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModel extends AndroidViewModel {

    private MovieRepo movieRepo;
    private LiveData<List<Movie>> favMovies;

    public ViewModel(@NonNull Application application) {
        super(application);

        movieRepo = new MovieRepo(application);
        favMovies = movieRepo.getFavMovies();

    }
    public LiveData<List<Movie>> getFavMovies(){
        return favMovies;
    }

    public void insert(Movie movie){
        movieRepo.insert(movie);
    }

    public void delete(Movie movie){
        movieRepo.delete(movie);
    }
}
