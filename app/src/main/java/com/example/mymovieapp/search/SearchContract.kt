package com.example.mymovieapp.search

import com.example.mymovieapp.data.MovieDetail
import com.example.mymovieapp.room.MovieEntity

class SearchContract {

    interface PresenterInterface {
        fun searchMovies(query: String, year: String?)
        fun stop()
    }

    interface ViewInterface {
        fun showSearchResults(movies: List<MovieEntity>)
        fun showError(message: String)
        fun returnSelectedMovie(movie: MovieEntity)
    }
}
