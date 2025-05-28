package com.example.mymovieapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.domain.repository.MovieRepository
import com.example.mymovieapp.data.room.MovieEntity
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.usecase.AddMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addMovieUseCase: AddMovieUseCase
) : ViewModel() {

    fun addMovie(movie: Movie) {
        viewModelScope.launch {
            addMovieUseCase(movie)
        }
    }
}