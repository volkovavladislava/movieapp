package com.example.mymovieapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.adapter.SearchAdapter
import com.example.mymovieapp.data.MovieDetail
import com.example.mymovieapp.databinding.ActivitySearchBinding
import com.example.mymovieapp.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchAdapter
    private val apiService = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SearchAdapter(emptyList()) { selectedMovie ->
            val intent = Intent().apply {
                putExtra("selected_movie", selectedMovie)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        binding.recyclerSearch.layoutManager = LinearLayoutManager(this)
        binding.recyclerSearch.adapter = adapter

        val query = intent.getStringExtra("query") ?: ""
        val year = intent.getStringExtra("year")
        Log.d("SearchActivity", "Поисковый запрос: $query, год: $year")
        if(query.isNotEmpty()) {
            performSearch(query, year)
        } else {
            Toast.makeText(this, "Введите название фильма", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performSearch(query: String, year: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.searchMovies(search = query, year = year)
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    if (searchResponse?.Response == "True" && !searchResponse.Search.isNullOrEmpty()) {
                        val detailsList = mutableListOf<MovieDetail>()
                        searchResponse.Search.forEach { item ->
                            val detailResponse = apiService.getMovieDetail(imdbID = item.imdbID)
                            detailResponse.body()?.let { detailsList.add(it) }
                        }
                        withContext(Dispatchers.Main) {
                            adapter.updateMovies(detailsList)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SearchActivity, "Фильм не найден", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SearchActivity, "Ошибка запроса: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchActivity, "Произошла ошибка", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}