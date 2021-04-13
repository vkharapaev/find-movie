package com.headmostlab.findmovie.ui.view.main

import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.databinding.MovieRowItem2Binding
import com.headmostlab.findmovie.domain.entity.ShortMovie

class MovieViewHolder2Impl(
    private val view: View,
    private val binding: MovieRowItem2Binding = MovieRowItem2Binding.bind(view)
) :
    MovieViewHolder(view) {

    override fun bind(
        listener: MainFragment.OnItemClickedListener,
        categoryPosition: Int,
        movie: ShortMovie
    ) {
        with(binding) {
            movieId.text = movie.id.toString()
            title.text = movie.title
            year.text = movie.date
            rating.text = movie.rating.toString()
            poster.text = movie.poster
            root.setOnClickListener { listener.clicked(categoryPosition, adapterPosition) }
        }

        val imageUrl = TMDbImageHostProvider().getHostUrl() + movie.poster

        GlideApp.with(binding.root.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.posterImage)
    }

    override fun onRecycled() {
        GlideApp.with(binding.root.context).clear(binding.posterImage)
    }
}