package com.eltendawy.mymovies.Api.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

@Entity
public class Movie implements Parcelable {

    public static final String TAG="Movie";

	public Movie() {
	    favourite=false;
		numerReviews =0;
		lastReviewsPage=1;
	}

	@SerializedName("numerReviews")

	private int numerReviews;

	@SerializedName("lastReviewsPage")

	private int lastReviewsPage;

	@PrimaryKey
	@SerializedName("id")
	private int id;

	@SerializedName("genresString")
	private String genresString = "";
    @SerializedName("overview")
	private String overview;

    @ColumnInfo
    private boolean favourite;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("video")
	private boolean video;

	@SerializedName("title")
	private String title;

	@Ignore
	@SerializedName("genre_ids")
	private List<Integer> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("vote_average")
	private double voteAverage;

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("adult")
	private boolean adult;

	@SerializedName("vote_count")
	private int voteCount;

    protected Movie(Parcel in) {
        overview = in.readString();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        video = in.readByte() != 0;
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        id = in.readInt();
        numerReviews =in.readInt();
		lastReviewsPage =in.readInt();
        adult = in.readByte() != 0;
        voteCount = in.readInt();
        genreIds=new ArrayList<>();
        in.readList(genreIds,Integer.class.getClassLoader());
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

    public void setOverview(String overview){
		this.overview = overview;
	}

	public String getOverview(){
		return overview;
	}

	public void setOriginalLanguage(String originalLanguage){
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public void setOriginalTitle(String originalTitle){
		this.originalTitle = originalTitle;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public void setVideo(boolean video){
		this.video = video;
	}

	public boolean isVideo(){
		return video;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setGenreIds(List<Integer> genreIds){
		this.genreIds = genreIds;
	}

	public List<Integer> getGenreIds(){
		return genreIds;
	}

	public void setPosterPath(String posterPath){
		this.posterPath = posterPath;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public void setBackdropPath(String backdropPath){
		this.backdropPath = backdropPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public void setReleaseDate(String releaseDate){
		this.releaseDate = releaseDate;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public void setVoteAverage(double voteAverage){
		this.voteAverage = voteAverage;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public void setPopularity(double popularity){
		this.popularity = popularity;
	}

	public double getPopularity(){
		return popularity;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAdult(boolean adult){
		this.adult = adult;
	}

	public boolean isAdult(){
		return adult;
	}

	public void setVoteCount(int voteCount){
		this.voteCount = voteCount;
	}

	public int getVoteCount(){
		return voteCount;
	}

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getGenresString() {
		return genresString;
	}

	public void setGenresString(String genresString) {
		this.genresString = genresString;
	}

	public String getGenres()
    {
        if(genreIds==null)return genresString;
        if(genresString!=null&&genresString.length()!=0)
        	return genresString;
        ArrayList<Genre> genres=Genre.getGenre(genreIds);
        for (Genre genre : genres)
            genresString=genresString.concat(genre.getName()).concat("|");
        if(genresString.equals(""))return genresString;
        genresString=genresString.substring(0,genresString.length()-1);
        return genresString;

    }

	public int getNumerReviews() {
		return numerReviews;
	}

	public void setNumerReviews(int numerReviews) {
		this.numerReviews = numerReviews;
	}

	public int getLastReviewsPage() {
		return lastReviewsPage;
	}

	public void setLastReviewsPage(int lastReviewsPage) {
		this.lastReviewsPage = lastReviewsPage;
	}

	@NonNull
    @Override
 	public String toString(){
		return title;
		}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(overview);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
		dest.writeInt(id);
		dest.writeInt(numerReviews);
		dest.writeInt(lastReviewsPage);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeInt(voteCount);
        dest.writeList(genreIds);
    }
}