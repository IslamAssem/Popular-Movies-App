package com.eltendawy.mymovies.Api.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TrailersResponse{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<Trailer> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<Trailer> results){
		this.results = results;
	}

	public List<Trailer> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"TrailersResponse{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}