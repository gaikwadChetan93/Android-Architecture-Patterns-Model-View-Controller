package com.example.topratedmoviewitharchitecturepattern.top_rated

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.topratedmoviewitharchitecturepattern.database.MovieDatabase
import com.example.topratedmoviewitharchitecturepattern.ext.mapToDbMovie
import com.example.topratedmoviewitharchitecturepattern.ext.mapToMovie
import com.example.topratedmoviewitharchitecturepattern.model.Movie
import com.example.topratedmoviewitharchitecturepattern.network.MovieApi
import retrofit2.Retrofit

class MovieRepository(
    private val retrofit: Retrofit,
    private val db: MovieDatabase
) {

    private val movieResponseLiveData = MutableLiveData<List<Movie>>()

    fun getMovies(): MutableLiveData<List<Movie>> {
        return movieResponseLiveData
    }

    suspend fun fetchMovies() {
        //If movies available in cache the return from cache
        val movieDao = db.movieDao()
        val cachedMovies = movieDao.getAll()
        if (cachedMovies.isNotEmpty()) {
//            Log.d("Movie logs", "Movies available in cache")
            movieResponseLiveData.postValue(cachedMovies.mapToMovie())
        } else {
            //If movies not available in cache the fetch from the API, Update the cache and then return the result
            Log.d("Movie logs", "Movies fetching from API")
            val movieApi = retrofit.create(MovieApi::class.java)
            try {
                val movieResponse =
                    movieApi.fetchTopRatedMovies("YOUR TMDB API KEY")
                db.movieDao().addMovies(movieResponse.results.mapToDbMovie())
                movieResponseLiveData.postValue(movieResponse.results)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}