package com.headmostlab.findmovie.ui.view.main

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.headmostlab.findmovie.R
import com.headmostlab.findmovie.domain.entity.Collection

class FullCategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val seeCollectionButton: Button = view.findViewById(R.id.seeCollectionButton)
    fun bind(collection: Collection, showCollectionListener: (Collection) -> Unit) {
        seeCollectionButton.setOnClickListener {
            showCollectionListener.invoke(collection)
        }
    }
}
