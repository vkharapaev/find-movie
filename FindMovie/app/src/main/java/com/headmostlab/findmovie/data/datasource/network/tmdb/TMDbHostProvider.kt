package com.headmostlab.findmovie.network.tmdb

import com.headmostlab.findmovie.network.HostProvider

class TMDbHostProvider() : HostProvider {
    override fun getHostUrl() = "https://api.themoviedb.org/3/"
}