package com.example.mymovieapp.data.repository

import com.example.mymovieapp.data.mapper.toDomain
import com.example.mymovieapp.data.mapper.toEntity
import com.example.mymovieapp.data.room.MovieDao
import com.example.mymovieapp.data.service.OmdbApiService
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.model.MovieDetail
import com.example.mymovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val apiService: OmdbApiService
) : MovieRepository {

    override fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie.toEntity())
    }



    override suspend fun searchMovies(query: String, year: String?): Result<List<MovieDetail>> {
        return try {
            val response = apiService.searchMovies(search = query, year = year)
            if (response.isSuccessful) {
                val searchResponse = response.body()
                if (searchResponse?.Response == "True" && !searchResponse.Search.isNullOrEmpty()) {

                    val details = coroutineScope {
                        searchResponse.Search.map { item ->
                            async {
                                val detailResponse = apiService.getMovieDetail(imdbID = item.imdbID)
                                detailResponse.body()?.toDomain()
                            }
                        }.awaitAll().filterNotNull()
                    }

                    Result.success(details)
                } else {
                    Result.failure(Exception(searchResponse?.Error ?: "No movies found"))
                }
            } else {
                Result.failure(Exception("API request failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}