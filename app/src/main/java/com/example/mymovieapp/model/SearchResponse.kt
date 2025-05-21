package com.example.mymovieapp.model

data class SearchResponse(
    val Search: List<MovieItem>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)