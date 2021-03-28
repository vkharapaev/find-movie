package com.headmostlab.findmovie.network.tmdb

import com.google.gson.GsonBuilder
import com.headmostlab.findmovie.network.HostProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TMDbApi(private val hostProvider: HostProvider) {
    fun getService(): TMDbApiService {
        val gson = GsonBuilder().create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(hostProvider.getHostUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(TMDbApiService::class.java)
    }
}