package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.BuildConfig

class TMDbApiKeyProvider : ApiKeyProvider {
    override fun getApiKey() = BuildConfig.TMDB_API_KEY
}