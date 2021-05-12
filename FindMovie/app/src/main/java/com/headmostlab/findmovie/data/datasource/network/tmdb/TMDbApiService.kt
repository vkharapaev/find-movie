package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.credit.ApiCredits
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie.ApiFullMovie
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular.ApiMovies
import com.headmostlab.findmovie.data.datasource.network.tmdb.dto.video.ApiVideos
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApiService {

    @GET("3/movie/{rq}")
    fun getMovies(
        @Path("rq") rq: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("3/movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("3/movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int? = null
    ): Single<ApiMovies>

    @GET("3/movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<ApiFullMovie>

    @GET("3/movie/{movie_id}/videos")
    fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<ApiVideos>

    @GET("3/movie/{movie_id}/credits")
    fun getCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Single<ApiCredits>
}