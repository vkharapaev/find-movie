package com.headmostlab.findmovie.data.datasource.network.tmdb

interface ApiKeyProvider {
    fun getApiKey(): String
}