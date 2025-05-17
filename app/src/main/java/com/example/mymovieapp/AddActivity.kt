package com.example.mymovieapp


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mymovieapp.databinding.ActivityAddBinding
import androidx.activity.viewModels
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.MovieViewModel


class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private val viewModel: MovieViewModel by viewModels()
    private var selectedMovie: MovieEntity? = null

    companion object {
        const val REQUEST_CODE_SEARCH = 1001
        const val EXTRA_SELECTED_MOVIE = "selected_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener {
            val query = binding.etMovieName.text.toString().trim()
            if (query.isNotEmpty()) {
                val intent = Intent(this, com.example.mymovieapp.SearchActivity::class.java)
                intent.putExtra("query", query)
                intent.putExtra("year", binding.etMovieYear.text.toString().trim())
                startActivityForResult(intent, REQUEST_CODE_SEARCH)
            }
        }

        binding.btnAddMovie.setOnClickListener {
            selectedMovie?.let {
                viewModel.addMovie(it)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == Activity.RESULT_OK) {
            data?.getSerializableExtra(EXTRA_SELECTED_MOVIE)?.let { extra ->
                if (extra is MovieEntity) {
                    selectedMovie = extra
                    binding.etMovieName.setText(selectedMovie?.title)
                    binding.etMovieYear.setText(selectedMovie?.year)
                    binding.imgPoster.visibility = android.view.View.VISIBLE
                    Glide.with(this)
                        .load(selectedMovie?.posterUrl)
                        .into(binding.imgPoster)
                    binding.btnAddMovie.visibility = android.view.View.VISIBLE
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}