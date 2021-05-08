package com.headmostlab.findmovie.ui.view.main

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.domain.entity.ShortMovie

abstract class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(
            listener: (ShortMovie) -> Unit,
            movie: ShortMovie?
    )

    abstract fun onRecycled()
}