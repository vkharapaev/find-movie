package com.headmostlab.findmovie.data.datasource.network.tmdb.dto.popular

import com.google.gson.annotations.SerializedName

data class ApiMovies(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("results") val results: List<ApiShortMovie>,
)
