package com.headmostlab.findmovie.ui.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.databinding.CollectionRowItem2Binding
import com.headmostlab.findmovie.databinding.CollectionRowItemBinding
import com.headmostlab.findmovie.domain.entity.ShortMovie
import com.headmostlab.findmovie.ui.viewmodel.main.model.UiMovieCollection
import com.rubensousa.recyclerview.ScrollStateHolder

class CollectionAdapter(
    private val listener: (ShortMovie) -> Unit,
    private val scrollHolder: ScrollStateHolder,
    private val lifecycleOwner: LifecycleOwner,
    private val pagerErrorHandler: (Throwable) -> Unit,
) :
    RecyclerView.Adapter<CollectionViewHolder>() {

    var movieCollection: List<UiMovieCollection> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            R.layout.collection_row_item -> CollectionRowItemBinding.inflate(inflater).root
            else -> CollectionRowItem2Binding.inflate(inflater).root
        }
        return CollectionViewHolder(view, scrollHolder, listener, lifecycleOwner, pagerErrorHandler)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(movieCollection[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (movieCollection[position].showSecondLayout)
            R.layout.collection_row_item2 else R.layout.collection_row_item
    }

    override fun onViewRecycled(holder: CollectionViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun getItemCount(): Int {
        return movieCollection.size
    }

    override fun onViewDetachedFromWindow(holder: CollectionViewHolder) {
        holder.fixSnapPosition()
    }
}
