package com.headmostlab.findmovie.network

interface ApiKeyProvider {
    fun getApiKey(): String
}