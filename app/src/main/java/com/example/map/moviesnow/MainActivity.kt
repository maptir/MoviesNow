package com.example.map.moviesnow

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private val INPUT_THEATER_REQUEST = 100
    private val INPUT_MOVIE_REQUEST = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            INPUT_MOVIE_REQUEST -> {
                if(resultCode != Activity.RESULT_CANCELED && data != null) {

                }
            }
            INPUT_THEATER_REQUEST -> {
                if(resultCode != Activity.RESULT_CANCELED && data != null) {

                }
            }
        }
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
