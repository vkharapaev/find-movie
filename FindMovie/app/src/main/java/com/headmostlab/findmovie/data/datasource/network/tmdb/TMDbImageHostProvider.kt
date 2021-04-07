package com.headmostlab.findmovie.network.tmdb

import com.headmostlab.findmovie.network.HostProvider

class TMDbImageHostProvider() : HostProvider {
    override fun getHostUrl() = "https://image.tmdb.org/t/p/w500"
}