package com.headmostlab.findmovie.data.datasource.local.entities

import androidx.room.*

@Entity(
    primaryKeys = ["collectionId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = Collection::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectionMovieCrossRef(
    val collectionId: Int,
    val movieId: Int
)
