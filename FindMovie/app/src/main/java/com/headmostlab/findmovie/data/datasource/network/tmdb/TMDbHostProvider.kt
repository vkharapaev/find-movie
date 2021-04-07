package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.data.datasource.network.HostProvider

class TMDbHostProvider() : HostProvider {
    override fun getHostUrl() = "https://api.tmdb.org/3/"
}