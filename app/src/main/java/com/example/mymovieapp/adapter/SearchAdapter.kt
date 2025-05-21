package com.example.mymovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ItemSearchBinding
import com.example.mymovieapp.model.MovieDetail
import com.example.mymovieapp.room.MovieEntity


class SearchAdapter(
    private var movies: List<MovieDetail>,
    private val onItemClick: (MovieEntity) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDetail) {
            binding.tvSearchTitle.text = movie.Title
            binding.tvSearchYear.text = movie.Year
            binding.tvSearchGenre.text = movie.Genre

            Glide.with(binding.imgSearchPoster.context)
                .load(movie.Poster)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.imgSearchPoster)

            binding.root.setOnLongClickListener {
                val selectedMovie = MovieEntity(
                    imdbID = movie.imdbID,
                    title = movie.Title,
                    year = movie.Year,
                    posterUrl = movie.Poster
                )
                onItemClick(selectedMovie)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<MovieDetail>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}