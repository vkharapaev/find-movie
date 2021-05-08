package com.headmostlab.findmovie.ui.viewmodel.main.model

import com.headmostlab.findmovie.domain.entity.Collection
import com.headmostlab.findmovie.domain.entity.ShortMovie

sealed class UiItem {
    class Movie(val movie: ShortMovie) : UiItem()
    class Footer(val collection: Collection) : UiItem()
}
