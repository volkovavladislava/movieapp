package com.example.mymovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import com.example.mymovieapp.data.MovieDetail
import com.example.mymovieapp.room.MovieEntity

class SearchAdapter(
    private var movies: List<MovieDetail>,
    private val onItemClick: (MovieEntity) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgSearchPoster)
        val tvTitle: TextView = itemView.findViewById(R.id.tvSearchTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvSearchYear)
        val tvGenre: TextView = itemView.findViewById(R.id.tvSearchGenre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movieDetail = movies[position]
        holder.tvTitle.text = movieDetail.Title
        holder.tvYear.text = movieDetail.Year
        holder.tvGenre.text = movieDetail.Genre
        Glide.with(holder.itemView.context)
            .load(movieDetail.Poster)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imgPoster)

        holder.itemView.setOnLongClickListener {
            val selectedMovie = MovieEntity(
                imdbID = movieDetail.imdbID,
                title = movieDetail.Title,
                year = movieDetail.Year,
                posterUrl = movieDetail.Poster
            )
            onItemClick(selectedMovie)
            true
        }
    }

    fun updateMovies(newMovies: List<MovieDetail>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}