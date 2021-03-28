package com.headmostlab.findmovie.network.tmdb

import com.headmostlab.findmovie.model.apimodel.movie.ApiFullMovie
import com.headmostlab.findmovie.model.apimodel.popular.ApiMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApiService {
    @GET("movie/popular")
    fun getMovies(@Query("api_key") apiKey: String): Single<ApiMovies>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): Single<ApiFullMovie>
}