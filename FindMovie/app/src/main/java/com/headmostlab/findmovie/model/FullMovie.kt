package com.headmostlab.findmovie.model

data class FullMovie(
    val id: Int,
    val title: String,
    val origTitle: String,
    val genres: List<String>,
    val duration: Int,
    val rating: Double,
    val budget: Int,
    val revenue: Int,
    val year: Int,
    val description: String,
    val poster: String
)