package com.headmostlab.findmovie.data.datasource.network.tmdb.dto.movie

import com.google.gson.annotations.SerializedName

data class ApiProductionCompany(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("origin_country") val originCountry: String
)