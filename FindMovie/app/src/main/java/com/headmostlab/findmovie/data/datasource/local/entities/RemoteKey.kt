package com.headmostlab.findmovie.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKey(
    @PrimaryKey
    val label: String,
    val nextKey: Int?
)
