package com.headmostlab.findmovie.domain.entity

data class ShortMovie(
    val id: Int,
    val title: String,
    val date: String,
    val rating: Double,
    val popularity: Double,
    val poster: String?,
    val backdrop: String?
)