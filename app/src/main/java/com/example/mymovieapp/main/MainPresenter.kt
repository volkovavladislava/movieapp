package com.example.mymovieapp.main


import android.util.Log
import com.example.mymovieapp.room.LocalDataSource
import com.example.mymovieapp.room.MovieDatabase
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.MovieViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainPresenter(
    private var viewInterface: MainContract.ViewInterface,
    private var dataSource: LocalDataSource): MainContract.PresenterInterface  {
    private val TAG = "MainPresenter"


    private val compositeDisposable = CompositeDisposable()

    //1
    val myMoviesObservable: Observable<List<MovieEntity>>
        get() = dataSource.allMovies

    //2
    val observer: DisposableObserver<List<MovieEntity>>
        get() = object : DisposableObserver<List<MovieEntity>>() {

            override fun onNext(movieList: List<MovieEntity>) {
                if (movieList == null || movieList.size == 0) {
                    viewInterface.displayNoMovies()
                } else {
                    viewInterface.displayMovies(movieList)
                }
            }
            override fun onError(@NonNull e: Throwable) {
                Log.d(TAG, "Error fetching movie list.", e)
                viewInterface.displayError("Error fetching movie list.")
            }
            override fun onComplete() {
                Log.d(TAG, "Completed")
            }

        }

    //3
    override fun getMyMoviesList() {
        val myMoviesDisposable = myMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        compositeDisposable.add(myMoviesDisposable)
    }

    override fun stop() {
        compositeDisposable.clear()
    }




    override fun deleteMovie(movie: MovieEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.deleteMovie(movie)
        }
    }


    override fun addMovie(movie: MovieEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.insertMovie(movie)
        }
    }

    override fun showDeleteConfirmation(movie: MovieEntity) {
        viewInterface.showDeleteConfirmationDialog(movie)
    }

}

