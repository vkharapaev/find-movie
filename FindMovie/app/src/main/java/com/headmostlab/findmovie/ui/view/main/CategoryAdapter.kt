package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.databinding.MovieCategoryRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import com.rubensousa.recyclerview.ScrollStateHolder

class CategoryAdapter(
    private val listener: (ShortMovie) -> Unit,
    private val scrollHolder: ScrollStateHolder,
    private val lifecycleOwner: LifecycleOwner,
    private val pagerErrorHandler: (Throwable) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movieCollection: List<UiMovieCollection> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(
            MovieCategoryRowItemBinding.inflate(LayoutInflater.from(parent.context)),
            scrollHolder,
            listener,
            lifecycleOwner,
            pagerErrorHandler
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(movieCollection[position])
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is CategoryViewHolder) {
            holder.onRecycled()
        }
    }

    override fun getItemCount(): Int {
        return movieCollection.size
    }

}
