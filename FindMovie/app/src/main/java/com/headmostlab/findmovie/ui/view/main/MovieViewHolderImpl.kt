package com.headmostlab.findmovie.ui.view.main

import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.databinding.MovieRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie

class MovieViewHolderImpl(
    private val view: View,
    private val binding: MovieRowItemBinding = MovieRowItemBinding.bind(view)
) :
    MovieViewHolder(view) {

    override fun bind(
        listener: (ShortMovie) -> Unit,
        movie: ShortMovie?
    ) {

        if (movie == null) {
            GlideApp.with(binding.root.context)
                .load(R.drawable.dummy_movie)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.posterImage)
        } else {
            with(binding) {
                movieId.text = movie.id.toString()
                title.text = movie.title
                year.text = if (movie.date.length >= 3) {
                    movie.date.substring(0..3)
                } else {
                    movie.date
                }
                rating.text = movie.rating.toString()
                poster.text = movie.poster
                root.setOnClickListener { listener(movie) }
            }

            val imageUrl = TMDbImageHostProvider().getHostUrl() + movie.poster

            GlideApp.with(binding.root.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.posterImage)
        }
    }

    override fun onRecycled() {
        GlideApp.with(binding.root.context).clear(binding.posterImage)
    }
}
