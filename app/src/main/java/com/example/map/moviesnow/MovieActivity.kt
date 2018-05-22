package com.example.map.moviesnow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.map.moviesnow.adapter.MovieListAdapter
import com.example.map.moviesnow.models.MockMovieRepository
import com.example.map.moviesnow.models.Movie
import com.example.map.moviesnow.models.MovieRepository
import com.example.map.moviesnow.presenter.MoviePresenter
import com.example.map.moviesnow.presenter.MovieView
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity: AppCompatActivity(), MovieView {
    private lateinit var repository: MovieRepository
    private lateinit var presenter: MoviePresenter
    private lateinit var adapter: MovieListAdapter
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        repository = MockMovieRepository()
        presenter = MoviePresenter(this, repository)
        loadingPanel.visibility = View.GONE
        presenter.start()
        setSpinner()
    }

    override fun setMovieList(movies: ArrayList<Movie>) {
        adapter = MovieListAdapter(this, movies)
        movieList.adapter = adapter
    }

    override fun toggleLoading() {
        loadingPanel.visibility = if(loadingPanel.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun setSpinner() {
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayListOf("Major Cineplex Ratchayothin", "Esplanade Cineplex Ngamwongwan-Khaerai"))
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.searchTheater(spinner.selectedItem as String)
            }
        }
    }
}