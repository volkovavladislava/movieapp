package com.example.mymovieapp.add

import com.example.mymovieapp.room.LocalDataSource
import com.example.mymovieapp.room.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMoviePresenter(
    private var viewInterface: AddMovieContract.ViewInterface?,
    private var dataSource: LocalDataSource
) : AddMovieContract.PresenterInterface {

    override fun handleSearchRequest(query: String, year: String) {
        // Здесь должна быть логика поиска (если SearchActivity тоже использует MVP)
        // Пока просто делегируем Activity открытие SearchActivity
//        viewInterface?.showSearchError("Search logic should be implemented")
        when {
            query.isEmpty() -> viewInterface?.showSearchError("Введите название фильма")
            else -> viewInterface?.openSearchScreen(query, year) // Делегируем View открытие экрана
        }
    }

    override fun handleAddMovie(movie: MovieEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.insertMovie(movie)
            withContext(Dispatchers.Main) {
                viewInterface?.showAddSuccess()
                viewInterface?.returnToMain()
            }
        }
    }

    fun detachView() {
        viewInterface = null
    }
}
