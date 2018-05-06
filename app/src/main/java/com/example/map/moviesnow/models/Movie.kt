package com.example.map.moviesnow.models

import java.io.Serializable

class Movie(val movieTitle: String, val movieDescription: String, val cinema: String, val time: String, val image:String):Serializable {

    override fun toString(): String {
        return movieTitle + "\n" +
                movieDescription + "\n" +
                cinema + "\n" +
                time + "\n" +
                image
    }

}