package com.headmostlab.findmovie.data.repository

import com.headmostlab.findmovie.data.datasource.network.youtube.YouTubeDataSource
import io.reactivex.Single
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(val dataSource: YouTubeDataSource) : VideoRepository {
    override fun getVideoUrl(videoId: String): Single<String> {
        return dataSource.getVideoUrl(videoId)
    }
}