package com.example.mymovieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.repository.MovieRepository
import com.example.mymovieapp.room.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    val movies: LiveData<List<MovieEntity>> = repository.getAllMovies().asLiveData()

    fun deleteMovie(movie: MovieEntity) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }

    fun undoDelete(movie: MovieEntity) = viewModelScope.launch {
        repository.addMovie(movie)
    }

    fun deleteMoviesByIds(ids: List<String>) = viewModelScope.launch {
        repository.deleteMoviesByIds(ids)
    }
}