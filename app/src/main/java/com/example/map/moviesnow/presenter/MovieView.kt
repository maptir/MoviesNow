package com.example.map.moviesnow.presenter

import com.example.map.moviesnow.models.Movie

interface MovieView {
    fun setMovieList(movies: ArrayList<Movie>)

    fun toggleLoading()
}