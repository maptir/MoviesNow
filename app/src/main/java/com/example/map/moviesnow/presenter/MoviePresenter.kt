package com.example.map.moviesnow.presenter

import com.example.map.moviesnow.models.MovieRepository
import java.util.*

class MoviePresenter(val view:MovieView, val repository: MovieRepository): Observer {
    fun start() {
        repository.addObserver(this)
        repository.loadAllTheater()
    }

    override fun update(o: Observable?, arg: Any?) {
        if(o == repository)
            view.setMovieList(repository.getMovieList())
    }

}