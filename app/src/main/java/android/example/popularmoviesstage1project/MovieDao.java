package android.example.popularmoviesstage1project;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> loadAllFav();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToFav(Movie movie);

    @Delete
    void removeFav(Movie movie);

    @Query("Select * from Movie where id= :id")
    public Movie getSingleMovie(int id);


}
