package com.example.mymovieapp.data

data class SearchResponse(
    val Search: List<MovieItem>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)