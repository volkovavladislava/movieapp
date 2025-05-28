package com.example.mymovieapp.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapp.data.model.MovieDetailDto
import com.example.mymovieapp.domain.model.MovieDetail
import com.example.mymovieapp.domain.repository.MovieRepository
import com.example.mymovieapp.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
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
            val result = searchMoviesUseCase(query, year)
            _searchResults.value = result.getOrNull() ?: emptyList()
            _error.value = result.exceptionOrNull()?.message
            _isLoading.value = false
        }
    }
}