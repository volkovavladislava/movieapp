package com.example.mymovieapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.model.MovieDetail
import com.example.mymovieapp.repository.MovieRepository
import com.example.mymovieapp.room.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private val _searchResults = MutableLiveData<List<MovieDetail>>()
    val searchResults: LiveData<List<MovieDetail>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun searchMovies(query: String, year: String?) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.searchMovies(query, year)
            _searchResults.value = result.getOrNull() ?: emptyList()
            _error.value = result.exceptionOrNull()?.message

            _isLoading.value = false
        }
    }
}