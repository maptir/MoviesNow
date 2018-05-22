package com.example.map.moviesnow.presenter

import com.example.map.moviesnow.models.Movie

interface TheaterView {
    fun setGoogleMap(movies: ArrayList<Movie>)

    fun toggleLoading()
}