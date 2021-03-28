package com.headmostlab.findmovie.model.apimodel.movie

import com.google.gson.annotations.SerializedName

data class ApiSpokenLanguage(
    @SerializedName("iso_639_1") val iso_639_1: String,
    @SerializedName("name") val name: String
)