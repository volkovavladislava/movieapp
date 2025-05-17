package com.example.mymovieapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.adapter.MovieAdapter
import com.example.mymovieapp.databinding.ActivityMainBinding
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter: MovieAdapter by lazy {
        MovieAdapter(
            movies = emptyList(),
            onDeleteClick = { movie ->
                showDeleteConfirmationDialog(movie)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFab()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.recyclerMovies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupFab() {
        binding.fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.movies.observe(this, Observer { movies ->
            if (movies.isNullOrEmpty()) {
                showEmptyView()
            } else {
                showMoviesList(movies)
            }
        })
    }

    private fun showEmptyView() {
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerMovies.visibility = View.GONE
    }

    private fun showMoviesList(movies: List<MovieEntity>) {
        binding.emptyView.visibility = View.GONE
        binding.recyclerMovies.visibility = View.VISIBLE
        adapter.updateMovies(movies)
    }

    private fun showDeleteConfirmationDialog(movie: MovieEntity) {
        AlertDialog.Builder(this)
            .setTitle("Удалить фильм")
            .setMessage("Вы уверены, что хотите удалить \"${movie.title}\" из избранного?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteMovie(movie)
                showUndoSnackbar(movie)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showUndoSnackbar(movie: MovieEntity) {
        Snackbar.make(binding.root, "Фильм удален", Snackbar.LENGTH_LONG)
            .setAction("Отменить") {
                viewModel.addMovie(movie)
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}