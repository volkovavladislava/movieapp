package com.example.mymovieapp.domain.model

data class MovieDetail(
    val title: String,
    val year: String,
    val genre: String,
    val posterUrl: String,
    val imdbID: String
)