package com.headmostlab.findmovie.ui.view.detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.headmostlab.findmovie.domain.entity.Person

class ActorAdapter : ListAdapter<Person, ActorViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ActorViewHolder.newInstance(parent)

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
        }
    }
}