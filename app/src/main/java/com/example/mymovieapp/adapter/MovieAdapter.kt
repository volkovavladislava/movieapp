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
import com.example.mymovieapp.room.MovieEntity

class MovieAdapter(
    private var movies: List<MovieEntity>,
    private val onDeleteClick: (movie: MovieEntity) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        // Загружаем данные о фильме
        holder.tvTitle.text = movie.title
        holder.tvYear.text = movie.year

        // Загружаем постер с помощью Glide
        Glide.with(holder.imgPoster.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imgPoster)

        // Обработка нажатия на кнопку удаления
        holder.btnDelete.setOnClickListener {
            onDeleteClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<MovieEntity>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}