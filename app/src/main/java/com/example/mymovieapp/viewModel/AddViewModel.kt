package com.example.mymovieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.repository.MovieRepository
import com.example.mymovieapp.room.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun addMovie(movie: MovieEntity) {
        viewModelScope.launch {
            repository.addMovie(movie)
        }
    }
}