package com.example.mymovieapp.domain.model

import java.io.Serializable

data class Movie(
    val imdbID: String,
    val title: String,
    val year: String,
    val posterUrl: String
): Serializable