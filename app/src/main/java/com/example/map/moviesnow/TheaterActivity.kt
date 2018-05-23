package com.example.map.moviesnow

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.map.moviesnow.models.Movie
import com.example.map.moviesnow.presenter.TheaterView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_theater.*
import android.location.LocationManager
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import com.example.map.moviesnow.models.MockMovieRepository
import com.example.map.moviesnow.models.MovieRepository
import com.example.map.moviesnow.presenter.TheaterPresenter
import org.w3c.dom.Text
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL

class TheaterActivity : AppCompatActivity(), OnMapReadyCallback, TheaterView {

    private lateinit var mMap: GoogleMap
    private lateinit var repository: MovieRepository
    private lateinit var presenter: TheaterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theater)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        repository = MockMovieRepository()
        presenter = TheaterPresenter(this, repository)
        presenter.start()
        loadingPanel.visibility = View.GONE
    }

    /**
     * This callback is triggered when the map is ready to be used.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        toggleLoading()
    }

    override fun setGoogleMap(movies: ArrayList<Movie>) {
        FindClosestCinema().execute(movies)
    }

    override fun toggleLoading() {
        loadingPanel.visibility = if (loadingPanel.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun getLocationFromAddress(strAddress: String): LatLng {
        val location = Geocoder(this).getFromLocationName(strAddress, 5)[0]
        return LatLng(location.latitude, location.longitude)
    }

    private fun getCurrentLocation(): Location? {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return null
        println("++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    private inner class FindClosestCinema : AsyncTask<ArrayList<Movie>, Unit, Movie?>() {
        override fun doInBackground(vararg params: ArrayList<Movie>): Movie? {
            var closest: Movie = params[0][0]
            println(params[0])
            val curLoc = getCurrentLocation() ?: return closest
            val currentLatLng = LatLng(curLoc.latitude, curLoc.longitude)
            for (movie in params[0]) {
                if (movie.cinema != closest.cinema) {
                    val movieLatLng = getLocationFromAddress(movie.cinema)
                    val closestLatLng = getLocationFromAddress(closest.cinema)
                    val dNew = FloatArray(1)
                    Location.distanceBetween(currentLatLng.latitude, movieLatLng.latitude, currentLatLng.longitude, movieLatLng.longitude, dNew)
                    val dOld = FloatArray(1)
                    Location.distanceBetween(currentLatLng.latitude, closestLatLng.latitude, currentLatLng.longitude, closestLatLng.longitude, dOld)
                    println("+++++++++++++++++++++++++++++++++++++++++++++++++++")
                    println(dNew[0])
                    println(dOld[0])
                    println("+++++++++++++++++++++++++++++++++++++++++++++++++++")
                    if (dNew[0] < dOld[0])
                        closest = movie
                }
            }
            return closest
        }

        override fun onPostExecute(result: Movie?) {
            if (result == null) {
                val loadText = loadingPanel.findViewById(R.id.loadText) as TextView
                loadText.text = "Cannot get your current location. \nTry Open LOCATION on your mobile \n open real GOOGLE MAP \n or going out of building."
                return
            }
            super.onPostExecute(result)
            val movieImage = movieInfo.findViewById(R.id.movie_image) as ImageView
            val movieTitle = movieInfo.findViewById(R.id.movie_title) as TextView
            val movieDes = movieInfo.findViewById(R.id.movie_des) as TextView
            val movieShowTime = movieInfo.findViewById(R.id.movie_showtime) as TextView

            movieTitle.text = result.movieTitle
            movieDes.text = result.movieDescription
            movieShowTime.text = (" " + result.time + " ")
            val input: InputStream?
            try {
                val url = URL(result.image)
                input = BufferedInputStream(url.openStream())
                val bitmap = BitmapFactory.decodeStream(input)
                val resized = Bitmap.createScaledBitmap(bitmap, (bitmap.width * 0.39).toInt(), (bitmap.height * 0.39).toInt(), true);
                movieImage.setImageBitmap(resized)
                input.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val closest = getLocationFromAddress(result.cinema)
            val cLatLng = LatLng(closest.latitude, closest.longitude)
            mMap.addMarker(MarkerOptions().position(cLatLng).title(result.cinema))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cLatLng, 16f))
            toggleLoading()
        }
    }

}