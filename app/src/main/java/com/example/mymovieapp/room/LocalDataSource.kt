package com.example.mymovieapp.room

import android.app.Application
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.rx3.asObservable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocalDataSource(application: Application) {

    private val movieDao = MovieDatabase.getDatabase(application).movieDao()

    val allMovies: Observable<List<MovieEntity>>
        get() = movieDao.getAllMovies().asObservable()

    fun deleteMovie(movie: MovieEntity) {
        GlobalScope.launch {
            movieDao.deleteMovie(movie)
        }
    }

    fun insertMovie(movie: MovieEntity) {
        GlobalScope.launch {
            movieDao.insertMovie(movie)
        }
    }

//    fun deleteMoviesByIds(ids: List<String>) {
//        GlobalScope.launch {
//            movieDao.deleteMoviesByIds(ids)
//        }
//    }
}