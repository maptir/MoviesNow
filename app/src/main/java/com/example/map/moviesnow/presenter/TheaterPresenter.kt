package com.example.map.moviesnow.presenter

import com.example.map.moviesnow.models.MovieRepository
import java.util.*

class TheaterPresenter(val view: TheaterView, val repository: MovieRepository) : Observer {
    fun start() {
        repository.addObserver(this)
        repository.loadAllMovie()
    }

    override fun update(o: Observable?, arg: Any?) {
        if (o == repository) {
            view.setGoogleMap(repository.getMovieList())
        }
    }

}