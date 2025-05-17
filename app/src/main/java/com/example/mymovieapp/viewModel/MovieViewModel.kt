package com.example.mymovieapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.room.MovieDatabase
import com.example.mymovieapp.room.MovieEntity
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao = MovieDatabase.getDatabase(application).movieDao()

    val movies = movieDao.getAllMovies().asLiveData()

    fun deleteMovie(movie: MovieEntity) = viewModelScope.launch {
        movieDao.deleteMovie(movie)
    }

    fun addMovie(movie: MovieEntity) = viewModelScope.launch {
        movieDao.insertMovie(movie)
    }

    fun deleteMovies(selectedIds: List<String>) = viewModelScope.launch {
        movieDao.deleteMoviesByIds(selectedIds)
    }
}