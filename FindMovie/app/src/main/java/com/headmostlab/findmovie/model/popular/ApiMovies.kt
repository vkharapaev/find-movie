package com.headmostlab.findmovie.model.apimodel.popular

import com.google.gson.annotations.SerializedName
import com.headmostlab.findmovie.model.apimodel.popular.ApiShortMovie

data class ApiMovies(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("results") val results: List<ApiShortMovie>,
)
