package com.example.mymovieapp.di

import android.content.Context
import androidx.room.Room
import com.example.mymovieapp.data.repository.MovieRepositoryImpl
import com.example.mymovieapp.domain.repository.MovieRepository
import com.example.mymovieapp.data.room.MovieDao
import com.example.mymovieapp.data.room.MovieDatabase
import com.example.mymovieapp.data.service.OmdbApiService
import com.example.mymovieapp.domain.usecase.AddMovieUseCase
import com.example.mymovieapp.domain.usecase.DeleteMovieUseCase
import com.example.mymovieapp.domain.usecase.GetAllMoviesUseCase
import com.example.mymovieapp.domain.usecase.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideOmdbApiService(retrofit: Retrofit): OmdbApiService =
        retrofit.create(OmdbApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db").build()

    @Provides
    fun provideDao(database: MovieDatabase): MovieDao = database.movieDao()

    @Provides
    @Singleton
    fun provideMovieRepository(
        dao: MovieDao,
        api: OmdbApiService
    ): MovieRepository = MovieRepositoryImpl(dao, api)

    @Provides fun provideAddMovieUseCase(repo: MovieRepository) = AddMovieUseCase(repo)
    @Provides fun provideDeleteMovieUseCase(repo: MovieRepository) = DeleteMovieUseCase(repo)
    @Provides fun provideGetAllMoviesUseCase(repo: MovieRepository) = GetAllMoviesUseCase(repo)
    @Provides fun provideSearchMoviesUseCase(repo: MovieRepository) = SearchMoviesUseCase(repo)
}