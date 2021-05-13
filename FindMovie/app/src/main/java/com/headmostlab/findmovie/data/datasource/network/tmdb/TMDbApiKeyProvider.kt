package com.headmostlab.findmovie.data.datasource.network.tmdb

import com.headmostlab.findmovie.BuildConfig
import javax.inject.Inject

class TMDbApiKeyProvider @Inject constructor() : ApiKeyProvider {
    override fun getApiKey() = BuildConfig.TMDB_API_KEY
}