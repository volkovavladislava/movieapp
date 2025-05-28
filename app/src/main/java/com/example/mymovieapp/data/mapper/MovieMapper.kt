package com.example.mymovieapp.data.mapper

import com.example.mymovieapp.data.model.MovieDetailDto
import com.example.mymovieapp.data.room.MovieEntity
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.domain.model.MovieDetail



fun MovieEntity.toDomain(): Movie {
    return Movie(
        imdbID = imdbID,
        title = title,
        year = year,
        posterUrl = posterUrl
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        imdbID = imdbID,
        title = title,
        year = year,
        posterUrl = posterUrl
    )
}
fun MovieDetailDto.toDomain(): MovieDetail {
    return MovieDetail(
        title = this.Title,
        year = this.Year,
        genre = this.Genre,
        posterUrl = this.Poster,
        imdbID = this.imdbID
    )
}

