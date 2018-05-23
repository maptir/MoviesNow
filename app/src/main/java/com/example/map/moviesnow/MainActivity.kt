package com.example.map.moviesnow

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View

class MainActivity : AppCompatActivity() {

    private val INPUT_THEATER_REQUEST = 100
    private val INPUT_MOVIE_REQUEST = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    fun onClickTheaterButton(view: View) {
        val intent = Intent(this, TheaterActivity::class.java)
        startActivityForResult(intent, INPUT_THEATER_REQUEST)
    }

    fun onClickMovieButton(view: View) {
        val intent = Intent(this, MovieActivity::class.java)
        startActivityForResult(intent, INPUT_MOVIE_REQUEST)
    }
}
