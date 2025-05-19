package com.example.mymovieapp.main


import com.example.mymovieapp.room.MovieEntity

class MainContract {

    interface PresenterInterface {
        fun getMyMoviesList()
        fun stop()


        fun deleteMovie(movie: MovieEntity)
        fun addMovie(movie: MovieEntity)
        fun showDeleteConfirmation(movie: MovieEntity)

    }

    interface ViewInterface {
        fun  displayMovies (movieList: List <MovieEntity>)
        fun  displayNoMovies ()
        fun  displayMessage (message: String )
        fun  displayError (message: String )

        fun showDeleteConfirmationDialog(movie: MovieEntity)
        fun showUndoSnackbar(movie: MovieEntity)

    }
}
