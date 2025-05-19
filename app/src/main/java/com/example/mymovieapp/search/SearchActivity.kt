package com.example.mymovieapp.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.adapter.SearchAdapter
import com.example.mymovieapp.data.MovieDetail
import com.example.mymovieapp.databinding.ActivitySearchBinding
import com.example.mymovieapp.retrofit.RetrofitClient
import com.example.mymovieapp.room.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity(), SearchContract.ViewInterface  {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchAdapter
    private val apiService = RetrofitClient.apiService

    private lateinit var searchPresenter: SearchPresenter

    private val detailToEntityMap = mutableMapOf<MovieDetail, MovieEntity>()

    private fun setupPresenter() {
        searchPresenter = SearchPresenter(this, apiService)
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(emptyList()) { movieEntity ->
            returnSelectedMovie(movieEntity)
        }
        binding.recyclerSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPresenter()
        setupRecyclerView()

        val query = intent.getStringExtra("query") ?: ""
        val year = intent.getStringExtra("year")

        if (query.isNotEmpty()) {
            searchPresenter.searchMovies(query, year)
        } else {
            showError("Введите название фильма")
        }

    }

    override fun showSearchResults(movies: List<MovieEntity>) {
        val detailList = movies.map { entity ->
            MovieDetail(
                Title = entity.title, Year = entity.year, Rated = "", Released = "", Runtime = "", Genre = "", Director = "", Writer = "", Actors = "",
                Plot = "", Language = "", Country = "", Awards = "", Poster = entity.posterUrl, imdbID = entity.imdbID, Type = "movie", Response = "True"
            ).also { detail ->
                detailToEntityMap[detail] = entity
            }
        }
        adapter.updateMovies(detailList)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun returnSelectedMovie(movie: MovieEntity) {
        val intent = Intent().apply {
            putExtra("selected_movie", movie)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.stop()
    }


}