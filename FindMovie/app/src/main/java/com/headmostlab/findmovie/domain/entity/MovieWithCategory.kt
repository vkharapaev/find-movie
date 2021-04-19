package com.headmostlab.findmovie.domain.entity

data class MovieWithCategory (val category: MovieCategory, val movies: List<ShortMovie>)