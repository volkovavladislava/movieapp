package com.example.mymovieapp.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.R
import com.example.mymovieapp.adapter.MovieAdapter
import com.example.mymovieapp.add.AddActivity
import com.example.mymovieapp.databinding.ActivityMainBinding
import com.example.mymovieapp.main.MainContract
import com.example.mymovieapp.main.MainPresenter
import com.example.mymovieapp.room.LocalDataSource
import com.example.mymovieapp.room.MovieEntity
import com.example.mymovieapp.viewModel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),  MainContract.ViewInterface  {

    private lateinit var mainPresenter: MainContract.PresenterInterface

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter: MovieAdapter by lazy {
        MovieAdapter(
            movies = emptyList(),
            onDeleteClick = { movie ->
//                showDeleteConfirmationDialog(movie)
                mainPresenter.showDeleteConfirmation(movie)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPresenter()
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
                displayNoMovies()
            } else {
                displayMovies(movies)
            }
        })
    }

    private fun setupPresenter() {
        val dataSource = LocalDataSource(application = application)
        mainPresenter = MainPresenter(this, dataSource)
    }


    override fun displayNoMovies() {
        Log.d(TAG, "No movies to display.")
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerMovies.visibility = View.GONE
    }



    override fun displayMovies(movieList: List<MovieEntity>) {
        adapter.updateMovies(movieList)
        binding.emptyView.visibility = View.GONE
        binding.recyclerMovies.visibility = View.VISIBLE
    }


    override  fun showDeleteConfirmationDialog(movie: MovieEntity) {
        AlertDialog.Builder(this)
            .setTitle("Удалить фильм")
            .setMessage("Вы уверены, что хотите удалить \"${movie.title}\" из избранного?")
            .setPositiveButton("Удалить") { _, _ ->
                mainPresenter.deleteMovie(movie)
                showUndoSnackbar(movie)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override  fun showUndoSnackbar(movie: MovieEntity) {
        Snackbar.make(binding.root, "Фильм удален", Snackbar.LENGTH_LONG)
            .setAction("Отменить") {
                mainPresenter.addMovie(movie)
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
//                mainPresenter.onDeleteTapped(adapter.selectedMovies)
                true // Возвращаем true, т.к. событие обработано
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onStart() {
        super.onStart()
        mainPresenter.getMyMoviesList()
    }



    override fun displayMessage(message: String ) {
        Toast.makeText(this@MainActivity , message, Toast. LENGTH_LONG )
            .show()
    }

    override fun displayError (message: String ) {
        displayMessage (message)
    }


    override fun onStop() {
        super.onStop()
        mainPresenter.stop()
    }


}