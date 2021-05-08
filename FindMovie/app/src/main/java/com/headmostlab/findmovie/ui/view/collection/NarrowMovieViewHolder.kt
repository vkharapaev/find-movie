package com.headmostlab.findmovie.ui.view.collection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.databinding.NarrowMovieRowItemBinding
import com.headmostlab.findmovie.databinding.NarrowMovieRowItemInGridBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie

class NarrowMovieViewHolder(
    private val view: View,
    private val binding: NarrowMovieRowItemInGridBinding = NarrowMovieRowItemInGridBinding.bind(view)
) :
    RecyclerView.ViewHolder(view) {

    fun bind(
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

            val imageUrl = TMDbImageHostProvider().getHostUrl() + movie.poster

            val resources = binding.root.resources
            GlideApp.with(App.instance)
                .load(imageUrl)
                .override(
                    resources.getDimensionPixelSize(R.dimen.narrow_poster_width),
                    resources.getDimensionPixelSize(R.dimen.narrow_poster_height)
                )
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.card_radius))
                ).placeholder(R.drawable.bg_item_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.posterImage)
        }
    }

    fun onRecycled() {
        GlideApp.with(App.instance).clear(binding.posterImage)
    }
}
