package com.headmostlab.findmovie.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("request", unique = true)])
data class Collection(
    @PrimaryKey
    val id: Int,
    val collectionRid: String,
    val request: String
)
