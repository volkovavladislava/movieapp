package com.example.mymovieapp.domain.usecase

import com.example.mymovieapp.domain.model.MovieDetail
import com.example.mymovieapp.domain.repository.MovieRepository

class SearchMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(query: String, year: String?): Result<List<MovieDetail>> {
        return repository.searchMovies(query, year)
    }
}