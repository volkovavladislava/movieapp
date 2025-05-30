package com.example.mymovieapp.data.service

import com.example.mymovieapp.data.model.MovieDetailDto
import com.example.mymovieapp.data.model.SearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String = "a5922d23",
        @Query("s") search: String,
        @Query("y") year: String? = null
    ): Response<SearchResponseDto>

    @GET("/")
    suspend fun getMovieDetail(
        @Query("apikey") apiKey: String = "a5922d23",
        @Query("i") imdbID: String,
        @Query("plot") plot: String = "short"
    ): Response<MovieDetailDto>
}
