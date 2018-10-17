package com.eltendawy.mymovies.Api.Models;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GenresResponse {

	@SerializedName("genres")
	private List<Genre> genres;

	public void setGenres(List<Genre> genres){
		this.genres = genres;
	}

	public List<Genre> getGenres(){
		return genres;
	}

	@Override
 	public String toString(){
		return 
			"GenresResponse{" +
			"genres = '" + genres + '\'' + 
			"}";
		}
}