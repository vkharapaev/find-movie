package com.headmostlab.findmovie.data.datasource.network

interface ApiKeyProvider {
    fun getApiKey(): String
}