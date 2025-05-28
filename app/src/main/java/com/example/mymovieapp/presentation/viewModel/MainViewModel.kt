package com.example.mymovieapp.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.domain.repository.MovieRepository
import com.example.mymovieapp.data.room.MovieEntity
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.usecase.AddMovieUseCase
import com.example.mymovieapp.domain.usecase.DeleteMovieUseCase
import com.example.mymovieapp.domain.usecase.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllMoviesUseCase: GetAllMoviesUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val addMovieUseCase: AddMovieUseCase,
) : ViewModel() {

    val movies: LiveData<List<Movie>> = getAllMoviesUseCase().asLiveData()

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        deleteMovieUseCase(movie)
    }

    fun undoDelete(movie: Movie) = viewModelScope.launch {
        addMovieUseCase(movie)
    }
}