package com.example.map.moviesnow.models

import java.util.Observable

abstract class MovieRepository: Observable() {
    abstract fun loadAllTheater()

    abstract fun getMovieList(): ArrayList<Movie>
}