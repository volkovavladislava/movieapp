package com.example.mymovieapp.search

import com.example.mymovieapp.data.MovieDetail
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.service.OmdbApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPresenter(
    private var viewInterface: SearchContract.ViewInterface?,
    private val apiService: OmdbApiService
) : SearchContract.PresenterInterface {

    override fun searchMovies(query: String, year: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.searchMovies(search = query, year = year)
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    if (searchResponse?.Response == "True" && !searchResponse.Search.isNullOrEmpty()) {
                        val entityList = mutableListOf<MovieEntity>()

                        searchResponse.Search.forEach { item ->
                            val detailResponse = apiService.getMovieDetail(imdbID = item.imdbID)
                            detailResponse.body()?.let { detail ->
                                entityList.add(MovieEntity(
                                    imdbID = detail.imdbID,
                                    title = detail.Title,
                                    year = detail.Year,
                                    posterUrl = detail.Poster
                                ))
                            }
                        }

                        withContext(Dispatchers.Main) {
                            viewInterface?.showSearchResults(entityList)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            viewInterface?.showError("Фильмы не найдены")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        viewInterface?.showError("Ошибка запроса: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    viewInterface?.showError("Ошибка сети: ${e.message}")
                }
            }
        }
    }

    override fun stop() {
        viewInterface = null
    }
}