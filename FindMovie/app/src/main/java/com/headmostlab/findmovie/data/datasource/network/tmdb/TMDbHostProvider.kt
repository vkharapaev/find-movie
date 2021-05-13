package com.headmostlab.findmovie.data.datasource.network.tmdb

import javax.inject.Inject

class TMDbHostProvider @Inject constructor() : HostProvider {
    override fun getHostUrl() = "https://api.tmdb.org/"
}