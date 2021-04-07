package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieCategoryRowItemBinding
import com.headmostlab.findmovie.domain.entity.MovieWithCategory

class CategoryAdapter(
    private val listener: MainFragment.OnItemClickedListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movieCategories: List<MovieWithCategory> = ArrayList()
        set(value) {
            val start = field.size
            val count = value.size - field.size
            field = value
            notifyItemRangeChanged(start, count)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(
            MovieCategoryRowItemBinding.inflate(LayoutInflater.from(parent.context)),
            listener
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(movieCategories[position])
        }
    }

    override fun getItemCount(): Int {
        return movieCategories.size
    }

}
