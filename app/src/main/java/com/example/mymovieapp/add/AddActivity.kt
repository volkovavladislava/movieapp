package com.example.mymovieapp.add


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mymovieapp.databinding.ActivityAddBinding
import androidx.activity.viewModels
import com.example.mymovieapp.R
import com.example.mymovieapp.search.SearchActivity
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.AddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels()
    private var selectedMovie: MovieEntity? = null

    companion object {
        const val REQUEST_CODE_SEARCH = 1001
        const val EXTRA_SELECTED_MOVIE = "selected_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etMovieName.text.toString().trim()
            if (query.isNotEmpty()) {
                openSearchActivity(query)
            } else {
                showEmptyQueryError()
            }
        }

        binding.btnAddMovie.setOnClickListener {
            selectedMovie?.let { movie ->
                addMovieToCollection(movie)
            } ?: run {
                showNoMovieSelectedError()
            }
        }
    }

    private fun openSearchActivity(query: String) {
        val intent = Intent(this, SearchActivity::class.java).apply {
            putExtra("query", query)
            putExtra("year", binding.etMovieYear.text.toString().trim())
        }
        startActivityForResult(intent, REQUEST_CODE_SEARCH)
    }

    private fun addMovieToCollection(movie: MovieEntity) {
        viewModel.addMovie(movie)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SEARCH && resultCode == Activity.RESULT_OK) {
            handleSearchResult(data)
        }
    }

    private fun handleSearchResult(data: Intent?) {
        (data?.getSerializableExtra(EXTRA_SELECTED_MOVIE) as? MovieEntity)?.let { movie ->
            selectedMovie = movie
            updateUIWithSelectedMovie(movie)
        }
    }

    private fun updateUIWithSelectedMovie(movie: MovieEntity) {
        binding.apply {
            etMovieName.setText(movie.title)
            etMovieYear.setText(movie.year)
            imgPoster.visibility = View.VISIBLE
            btnAddMovie.visibility = View.VISIBLE

            Glide.with(this@AddActivity)
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(imgPoster)
        }
    }

    private fun showEmptyQueryError() {
        Toast.makeText(this, "Please enter movie title", Toast.LENGTH_SHORT).show()
    }

    private fun showNoMovieSelectedError() {
        Toast.makeText(this, "Please select a movie first", Toast.LENGTH_SHORT).show()
    }
}