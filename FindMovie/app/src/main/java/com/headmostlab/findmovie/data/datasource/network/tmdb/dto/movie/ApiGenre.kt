package com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie

import com.google.gson.annotations.SerializedName

data class ApiGenre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)