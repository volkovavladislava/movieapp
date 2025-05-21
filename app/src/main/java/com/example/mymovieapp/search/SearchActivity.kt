package com.example.mymovieapp.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.adapter.SearchAdapter
import com.example.mymovieapp.add.AddActivity
import com.example.mymovieapp.model.MovieDetail
import com.example.mymovieapp.databinding.ActivitySearchBinding
import com.example.mymovieapp.retrofit.RetrofitClient
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra("query") ?: ""
        val year = intent.getStringExtra("year")

        setupAdapter()
        setupObservers()

        if (query.isNotEmpty()) {
            viewModel.searchMovies(query, year)
        }
    }

    private fun setupAdapter() {
        adapter = SearchAdapter(emptyList()) { selectedMovie ->
            setResult(RESULT_OK, Intent().apply {
                putExtra(AddActivity.EXTRA_SELECTED_MOVIE, selectedMovie)
            })
            finish()
        }

        binding.recyclerSearch.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.searchResults.observe(this) { movies ->
            adapter.updateMovies(movies)
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}