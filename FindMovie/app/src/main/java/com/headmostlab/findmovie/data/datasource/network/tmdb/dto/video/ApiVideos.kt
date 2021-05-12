package com.headmostlab.findmovie.data.datasource.network.tmdb.dto.video

import com.google.gson.annotations.SerializedName

data class ApiVideos(
    @SerializedName("id") val movieId: Int,
    @SerializedName("results") val videos: List<ApiVideo>
)
