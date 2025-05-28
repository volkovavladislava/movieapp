package com.example.mymovieapp.presentation.add


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
import com.example.mymovieapp.presentation.search.SearchActivity
import com.example.mymovieapp.data.room.MovieEntity
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.presentation.viewModel.AddViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by viewModels()
    private var selectedMovie: Movie? = null

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
                Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddMovie.setOnClickListener {
            selectedMovie?.let {
                viewModel.addMovie(it)
                finish()
            } ?: Toast.makeText(this, "Выберите фильм", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openSearchActivity(query: String) {
        val intent = Intent(this, SearchActivity::class.java).apply {
            putExtra("query", query)
            putExtra("year", binding.etMovieYear.text.toString().trim())
        }
        startActivityForResult(intent, REQUEST_CODE_SEARCH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SEARCH && resultCode == Activity.RESULT_OK) {
            val movie = data?.getSerializableExtra(EXTRA_SELECTED_MOVIE) as? Movie
            movie?.let {
                selectedMovie = it
                updateUIWithSelectedMovie(it)
            }
        }
    }

    private fun updateUIWithSelectedMovie(movie: Movie) {
        binding.etMovieName.setText(movie.title)
        binding.etMovieYear.setText(movie.year)

        binding.imgPoster.visibility = View.VISIBLE
        binding.btnAddMovie.visibility = View.VISIBLE

        Glide.with(this)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.imgPoster)
    }
}