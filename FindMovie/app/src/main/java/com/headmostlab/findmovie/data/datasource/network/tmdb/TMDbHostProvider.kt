package com.headmostlab.findmovie.data.datasource.network.tmdb

class TMDbHostProvider() : HostProvider {
    override fun getHostUrl() = "https://api.tmdb.org/"
}