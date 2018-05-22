package com.example.map.moviesnow.models

import android.os.AsyncTask
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.StringReader
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class MockMovieRepository : MovieRepository() {

    private var movieList = ArrayList<Movie>()

    override fun getMovieList(): ArrayList<Movie> {
        return movieList
    }

    override fun loadAllMovie() {
        movieList.clear()
        TheaterLoaderTask().execute()
    }

    override fun filterMovieListByTheater(theater: String): ArrayList<Movie> {
        return movieList.filter { movie ->
            movie.cinema == theater
        } as ArrayList<Movie>
    }

    private inner class TheaterLoaderTask : AsyncTask<String, Unit, String>() {

        override fun doInBackground(vararg params: String?): String {
            val result = URL("https://raw.githubusercontent.com/zepalz/MoviesNow/master/assets/MoviesNow.json").readText()
            val klaxon = Klaxon()
            JsonReader(StringReader(result)).use { reader ->
                reader.beginArray {
                    val calendar = Calendar.getInstance()
                    val curMin = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
                    while (reader.hasNext()) {
                        klaxon.parse<Theater>(reader)?.let {
                            for (time in it.times) {
                                val hourMin = time.split(":")
                                if (curMin <= (hourMin[0].toInt() * 60 + hourMin[1].toInt()))
                                    movieList.add(Movie(it.movieTitle, it.movieDescription, it.cinema, time, it.image))
                            }
                        }
                    }
                }
            }
            movieList = ArrayList(movieList.sortedWith(compareBy({ it.time })))
            return "FINISHED"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            setChanged()
            notifyObservers()
        }
    }
}