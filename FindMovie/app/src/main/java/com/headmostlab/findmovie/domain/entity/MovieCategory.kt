package com.headmostlab.findmovie.domain.entity

import androidx.annotation.StringRes
import com.headmostlab.findmovie.R

enum class MovieCategory(@StringRes val title: Int) {
    NOW_PLAYING(R.string.movie_category_now_playing),
    UPCOMING(R.string.movie_category_upcoming),
    POPULAR(R.string.movie_category_popular)
}