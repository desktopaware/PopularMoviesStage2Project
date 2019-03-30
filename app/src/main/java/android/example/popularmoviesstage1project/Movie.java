package android.example.popularmoviesstage1project;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    //title, release date, movie poster, vote average, and plot synopsis
    String title, releaseDate, moviePoster, voteAverage, plotSynopsis;

    public Movie(){

    }

    public Movie(String title, String releaseDate, String moviePoster, String voteAverage, String plotSynopsis) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        moviePoster = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(moviePoster);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);
    }
}
