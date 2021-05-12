package com.headmostlab.findmovie.data.datasource.network.youtube

import io.reactivex.Single

interface YouTubeDataSource {
    fun getVideoUrl(videoId: String): Single<String>
}