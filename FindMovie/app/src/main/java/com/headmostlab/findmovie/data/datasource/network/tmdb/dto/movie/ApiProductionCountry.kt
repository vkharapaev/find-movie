package com.headmostlab.findmovie.model.apimodel.movie

import com.google.gson.annotations.SerializedName

data class ApiProductionCountry(
    @SerializedName("iso_3166_1") val iso_3166_1: String,
    @SerializedName("name") val name: String
)