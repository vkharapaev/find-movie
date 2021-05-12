package com.headmostlab.findmovie.data.datasource.network.tmdb

interface ImageHostProvider {
    fun getHostUrl(): String
    fun getProfileUrl(): String
}
