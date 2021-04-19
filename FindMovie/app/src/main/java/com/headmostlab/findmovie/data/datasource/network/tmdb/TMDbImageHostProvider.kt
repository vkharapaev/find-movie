package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.data.datasource.network.HostProvider

class TMDbImageHostProvider() : HostProvider {
    override fun getHostUrl() = "https://image.tmdb.org/t/p/w500"
}