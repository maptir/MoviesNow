package com.example.map.moviesnow.models

import java.util.Observable

abstract class MovieRepository: Observable() {
    abstract fun loadAllMovie()

    abstract fun getMovieList(): ArrayList<Movie>

    abstract fun filterMovieListByTheater(theater: String): ArrayList<Movie>
}