package com.headmostlab.findmovie.data.datasource.network.youtube

import android.content.Context
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile

class UTExtractor(
    context: Context,
    val listener: (ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) -> Unit
) : YouTubeExtractor(context) {
    override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
        listener.invoke(ytFiles, videoMeta)
    }
}