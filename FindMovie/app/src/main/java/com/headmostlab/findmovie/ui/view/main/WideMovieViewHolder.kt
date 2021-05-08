package com.headmostlab.findmovie.ui.view.main

import android.view.View
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.databinding.WideMovieRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie

class WideMovieViewHolder(
    private val view: View,
    private val binding: WideMovieRowItemBinding = WideMovieRowItemBinding.bind(view)
) :
    MovieViewHolder(view) {

    override fun bind(
        listener: (ShortMovie) -> Unit,
        movie: ShortMovie?
    ) {
        if (movie == null) {
            GlideApp.with(binding.root.context)
                .load(R.drawable.bg_item_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.posterImage)
        } else {
            with(binding) {
                title.text = movie.title
                root.setOnClickListener { listener(movie) }
            }

            val imageUrl = TMDbImageHostProvider().getHostUrl() + movie.backdrop

            val resources = binding.root.resources
            GlideApp.with(App.instance)
                .load(imageUrl)
                .override(
                    resources.getDimensionPixelSize(R.dimen.wide_poster_width),
                    resources.getDimensionPixelSize(R.dimen.wide_poster_height)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.card_radius))
                )
                .placeholder(R.drawable.bg_item_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.posterImage)
        }
    }

    override fun onRecycled() {
        GlideApp.with(App.instance).clear(binding.posterImage)
    }
}
