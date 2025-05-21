package com.example.mymovieapp.repository


import android.util.Log
import com.example.mymovieapp.model.MovieDetail
import com.example.mymovieapp.room.MovieDao
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.service.OmdbApiService
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val movieDao: MovieDao,
    private val apiService: OmdbApiService
) {

    fun getAllMovies(): Flow<List<MovieEntity>> = movieDao.getAllMovies()

    suspend fun addMovie(movie: MovieEntity) = movieDao.insertMovie(movie)

    suspend fun deleteMovie(movie: MovieEntity) = movieDao.deleteMovie(movie)

    suspend fun deleteMoviesByIds(ids: List<String>) = movieDao.deleteMoviesByIds(ids)


    suspend fun searchMovies(query: String, year: String?): Result<List<MovieDetail>> {
        return try {
            val response = apiService.searchMovies(search = query, year = year)
            if (response.isSuccessful) {
                val searchResponse = response.body()
                if (searchResponse?.Response == "True" && !searchResponse.Search.isNullOrEmpty()) {
                    val details = searchResponse.Search.mapNotNull { item ->
                        val detailResponse = apiService.getMovieDetail(imdbID = item.imdbID)
                        detailResponse.body()
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