package com.headmostlab.findmovie.ui.view.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.headmostlab.findmovie.App
import com.headmostlab.findmovie.GlideApp
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.data.datasource.network.tmdb.TMDbImageHostProvider
import com.headmostlab.findmovie.databinding.ActorRowItemBinding
import com.headmostlab.findmovie.domain.entity.Person

class ActorViewHolder(val binding: ActorRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

    private val imageProvider by lazy { TMDbImageHostProvider() }

    fun bind(person: Person) {

        val profileUrl = imageProvider.getProfileUrl() + person.profilePath

        val resources = binding.root.resources
        GlideApp.with(App.instance)
            .load(profileUrl)
            .placeholder(R.drawable.actor_avatar_placeholder)
            .override(
                resources.getDimensionPixelSize(R.dimen.actor_avatar_width),
                resources.getDimensionPixelSize(R.dimen.actor_avatar_height)
            )
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.avatar)

        binding.name.text = person.name

        binding.job.text = person.job
    }

    companion object {
        fun newInstance(parent: ViewGroup): ActorViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ActorRowItemBinding.inflate(inflater, parent, false)
            return ActorViewHolder(binding)
        }
    }
}