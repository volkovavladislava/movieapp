package com.example.mymovieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ItemSearchBinding
import com.example.mymovieapp.data.model.MovieDetailDto
import com.example.mymovieapp.data.room.MovieEntity
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.model.MovieDetail

class SearchAdapter(
    private var movies: List<MovieDetail>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDetail) {
            binding.tvSearchTitle.text = movie.title
            binding.tvSearchYear.text = movie.year
            binding.tvSearchGenre.text = movie.genre

            Glide.with(binding.imgSearchPoster.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.imgSearchPoster)

            binding.root.setOnLongClickListener {
                val selectedMovie = Movie(
                    imdbID = movie.imdbID,
                    title = movie.title,
                    year = movie.year,
                    posterUrl = movie.posterUrl
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