package com.headmostlab.findmovie.ui.view.utils

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

class VideoPlayer(val context: Context) {
    var player = initializePlayer()

    private var currentPosition = 0L

    fun stop() {
        currentPosition = player.currentPosition
        player.release()
        player = initializePlayer()
    }

    fun play(url: String) {
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.seekTo(currentPosition)
        player.volume = 0f
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.play()
    }

    private fun initializePlayer() = SimpleExoPlayer.Builder(context)
        .setTrackSelector(DefaultTrackSelector(context)).build()
}
