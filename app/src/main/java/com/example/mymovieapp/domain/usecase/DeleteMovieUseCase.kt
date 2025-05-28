package com.example.mymovieapp.domain.usecase

import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.repository.MovieRepository

class DeleteMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movie: Movie) = repository.deleteMovie(movie)
}