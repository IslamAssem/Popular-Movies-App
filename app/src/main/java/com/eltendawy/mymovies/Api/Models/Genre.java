package com.eltendawy.mymovies.Api.Models;

import android.media.tv.TvContract;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Genre implements Parcelable {
//todo create retrofit call as if the api is updated this may not work anymore
    public static final Genre[] genres =
            {new Genre(28, "Action"),
                    new Genre(12, "Adventure"),
                    new Genre(16, "Animation"),
                    new Genre(35, "Comedy"),
                    new Genre(80, "Crime"),
                    new Genre(99, "Documentary"),
                    new Genre(18, "Drama"),
                    new Genre(10751, "Family"),
                    new Genre(14, "Fantasy"),
                    new Genre(36, "History"),
                    new Genre(27, "Horror"),
                    new Genre(10402, "Music"),
                    new Genre(9648, "Mystery"),
                    new Genre(10749, "Romance"),
                    new Genre(878, "Science Fiction"),
                    new Genre(10770, "TV Movie"),
                    new Genre(53, "Thriller"),
                    new Genre(10752, "War"),
                    new Genre(37, "Western")
            };
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    public Genre() {
    }

    public Genre(int id, String name) {
        this.name = name;
        this.id = id;
    }

    protected Genre(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public static Genre getGenre(int id) {
        for (Genre genre : genres) {
            if (genre.getId() == id)
                return genre;
        }
        return null;
    }

    public static ArrayList<Genre> getGenre(List<Integer> genreIds) {
        ArrayList<Genre> genresFound = new ArrayList<>(5);
        for (int id : genreIds)
            for (Genre genre : genres) {
                if (genre.getId() == id)
                    genresFound.add(genre);
            }
        return genresFound;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "Genre{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }
}