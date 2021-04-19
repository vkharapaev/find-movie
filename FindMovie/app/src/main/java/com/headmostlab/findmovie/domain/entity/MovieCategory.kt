package com.headmostlab.findmovie.domain.entity

import androidx.annotation.StringRes
import com.headmostlab.findmovie.R

enum class MovieCategory(@StringRes val title: Int, val request: String) {
    NOW_PLAYING(R.string.movie_category_now_playing, "now_playing"),
    UPCOMING(R.string.movie_category_upcoming, "upcoming"),
    POPULAR(R.string.movie_category_popular, "popular"),
    TOP_RATED(R.string.movie_category_top_rated, "top_rated"),
}