package com.example.mymovieapp.domain.usecase

import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetAllMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> = repository.getAllMovies()
}