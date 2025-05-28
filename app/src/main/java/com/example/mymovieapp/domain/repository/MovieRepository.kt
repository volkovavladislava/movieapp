package com.example.mymovieapp.domain.repository


import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    fun getAllMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
    suspend fun searchMovies(query: String, year: String?): Result<List<MovieDetail>>
}