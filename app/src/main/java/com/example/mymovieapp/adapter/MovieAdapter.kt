package com.example.mymovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.ItemMovieBinding
import com.example.mymovieapp.room.MovieEntity


class MovieAdapter(
    private var movies: List<MovieEntity>,
    private val onDeleteClick: (MovieEntity) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            binding.tvTitle.text = movie.title
            binding.tvYear.text = movie.year

            Glide.with(binding.imgPoster.context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.imgPoster)

            binding.btnDelete.setOnClickListener {
                onDeleteClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<MovieEntity>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}