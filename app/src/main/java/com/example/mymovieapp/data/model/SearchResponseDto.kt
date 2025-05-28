package com.example.mymovieapp.data.model

data class SearchResponseDto(
    val Search: List<MovieItemDto>?,
    val totalResults: String?,
    val Response: String,
    val Error: String?
)