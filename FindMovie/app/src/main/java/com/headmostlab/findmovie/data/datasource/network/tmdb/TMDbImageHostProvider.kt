package com.headmostlab.findmovie.data.datasource.network.tmdb

class TMDbImageHostProvider : ImageHostProvider {
    override fun getHostUrl() = "https://image.tmdb.org/t/p/w300"
    override fun getProfileUrl() = "https://image.tmdb.org/t/p/w185"
}