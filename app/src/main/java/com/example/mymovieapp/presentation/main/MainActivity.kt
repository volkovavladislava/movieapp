package com.example.mymovieapp.presentation.main


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.R
import com.example.mymovieapp.presentation.adapter.MovieAdapter
import com.example.mymovieapp.presentation.add.AddActivity
import com.example.mymovieapp.databinding.ActivityMainBinding
import com.example.mymovieapp.data.room.MovieEntity
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.presentation.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupObservers()
        setupListeners()
    }

    private fun setupAdapter() {
        adapter = MovieAdapter(emptyList()) { movie ->
            showDeleteConfirmationDialog(movie)
        }

        binding.recyclerMovies.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovies.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.movies.observe(this) { movies ->
            if (movies.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerMovies.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerMovies.visibility = View.VISIBLE
                adapter.updateMovies(movies)
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private fun showDeleteConfirmationDialog(movie: Movie) {
        AlertDialog.Builder(this)
            .setTitle("Удалить фильм")
            .setMessage("Вы уверены, что хотите удалить \"${movie.title}\"?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteMovie(movie)
                showUndoSnackbar(movie)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showUndoSnackbar(movie: Movie) {
        Snackbar.make(binding.root, "Фильм удален", Snackbar.LENGTH_LONG)
            .setAction("Отменить") {
                viewModel.undoDelete(movie)
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