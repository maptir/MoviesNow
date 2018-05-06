package com.example.map.moviesnow.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.example.map.moviesnow.R
import com.example.map.moviesnow.models.Movie
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL

class MovieListAdapter(val context: Context, val movieList: ArrayList<Movie>): BaseAdapter(), ListAdapter{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.movie_info, null)
        }

        val movie: Movie = getItem(position) as Movie

        val movieImage = view!!.findViewById(R.id.movie_image) as ImageView
        val movieTitle = view.findViewById(R.id.movie_title) as TextView
        val movieDes = view.findViewById(R.id.movie_des) as TextView
        val movieShowTime = view.findViewById(R.id.movie_showtime) as TextView

        movieTitle.text = movie.movieTitle
        movieDes.text = movie.movieDescription
        movieShowTime.text = movie.time
        var input: InputStream? = null
        try {
            val url = URL(movie.image)
            println(movie.image)
            input = BufferedInputStream(url.openStream())
            movieImage.setImageBitmap(BitmapFactory.decodeStream(input))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (ignored: IOException) {
                    // Nothing to do
                }
            }
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return movieList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return movieList.size
    }
}