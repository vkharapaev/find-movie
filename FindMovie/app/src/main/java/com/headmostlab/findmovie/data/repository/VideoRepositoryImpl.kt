package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.youtube.YouTubeDataSource
import io.reactivex.Single

class VideoRepositoryImpl(val dataSource: YouTubeDataSource) : VideoRepository {
    override fun getVideoUrl(videoId: String): Single<String> {
        return dataSource.getVideoUrl(videoId)
    }
}