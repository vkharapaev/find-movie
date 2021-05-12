package com.headmostlab.findmovie.data.repository

import io.reactivex.Single

interface VideoRepository {
    fun getVideoUrl(videoId: String): Single<String>
}