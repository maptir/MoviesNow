package com.example.map.moviesnow.models

import android.os.AsyncTask
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.StringReader
import java.net.URL

class MockMovieRepository: MovieRepository() {

    private val theaterList = ArrayList<Theater>()
    private val movieList = ArrayList<Movie>()

    override fun getMovieList(): ArrayList<Movie> {
        return movieList
    }

    override fun loadAllTheater() {
        theaterList.clear()
        TheaterLoaderTask().execute()
    }

    inner class TheaterLoaderTask: AsyncTask<String, Unit, String>() {

        override fun doInBackground(vararg params: String?): String {
            return URL("https://raw.githubusercontent.com/zepalz/MoviesNow/master/assets/MoviesNow.json").readText()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(result != null) {
                val klaxon = Klaxon()
                JsonReader(StringReader(result)).use { reader ->
                    reader.beginArray {
                        while (reader.hasNext()) {
                            println(reader)
                            klaxon.parse<Theater>(reader)?.let { theaterList.add(it) }
                        }
                    }
                }
            }
            for(theater in theaterList) {
                for(time in theater.times) {
                    movieList.add(Movie(theater.movieTitle, theater.movieDescription, theater.cinema, time, theater.image))
                }
            }
            setChanged()
            notifyObservers()
        }
    }
}