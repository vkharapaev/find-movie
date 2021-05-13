package com.headmostlab.findmovie.data.datasource.network.youtube

import android.content.Context
import androidx.core.util.isEmpty
import io.reactivex.Single
import javax.inject.Inject

class YouTubeDataSourceImpl @Inject constructor(val context: Context) : YouTubeDataSource {
    override fun getVideoUrl(videoId: String): Single<String> {
        return Single.create { emitter ->
            UTExtractor(context) { ytFiles, _ ->
                if (ytFiles == null || ytFiles.isEmpty()) {
                    emitter.onError(IllegalArgumentException("Video $videoId is not found"))
                } else {
                    val ytFile = ytFiles.valueAt(0)
                    emitter.onSuccess(ytFile.url)
                }
            }.extract("https://www.youtube.com/watch?v=$videoId", true, true)
        }
    }
}