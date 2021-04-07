package com.headmostlab.findmovie.network.tmdb

import com.headmostlab.findmovie.BuildConfig
import com.headmostlab.findmovie.network.ApiKeyProvider

class TMDbApiKeyProvider : ApiKeyProvider {
    override fun getApiKey() = BuildConfig.TMDB_API_KEY
}