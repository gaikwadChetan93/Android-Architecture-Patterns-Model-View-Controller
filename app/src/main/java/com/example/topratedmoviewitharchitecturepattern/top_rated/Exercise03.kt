package com.example.topratedmoviewitharchitecturepattern.top_rated

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.topratedmoviewitharchitecturepattern.R
import com.example.topratedmoviewitharchitecturepattern.database.MovieDatabase
import com.example.topratedmoviewitharchitecturepattern.model.Movie
import com.example.topratedmoviewitharchitecturepattern.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class Exercise03 : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private val movies = arrayListOf<Movie>()
    lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initList()
        movieViewModel =
            ViewModelProvider(
                this,
                MovieViewModel.Factory(
                    MovieRepository(
                        RetrofitClient.getRetrofitClient(),
                        MovieDatabase.getInstance(application)
                    )
                )
            ).get(
                MovieViewModel::class.java
            )
        movieViewModel.getMovies().observe(this, Observer {
            movies.addAll(it)
            movieAdapter.notifyDataSetChanged()
        })
        movieViewModel.fetchMovies()
    }

    private fun initList() {
        movieList.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(this, movies)
        movieList.adapter = movieAdapter
    }
}
