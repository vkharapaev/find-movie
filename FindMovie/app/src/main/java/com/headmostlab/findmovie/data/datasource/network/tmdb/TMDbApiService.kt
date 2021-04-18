package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiFullMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApiService {

    @GET("movie/{rq}")
    fun getMovies(
        @Path("rq") rq: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<ApiFullMovie>
}