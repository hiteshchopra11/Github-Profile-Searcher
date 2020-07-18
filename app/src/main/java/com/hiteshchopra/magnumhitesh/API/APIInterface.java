package com.hiteshchopra.magnumhitesh.API;

import com.hiteshchopra.magnumhitesh.Models.GithubUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/search/users")
    Call<GithubUser>getUserDetails(@Query("q") String userNameQuery, @Query("page") int pageNumber);
}