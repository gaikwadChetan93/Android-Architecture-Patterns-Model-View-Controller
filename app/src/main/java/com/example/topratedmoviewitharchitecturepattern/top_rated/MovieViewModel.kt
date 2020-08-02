package com.example.topratedmoviewitharchitecturepattern.top_rated

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.topratedmoviewitharchitecturepattern.DefaultDispatcherProvider
import com.example.topratedmoviewitharchitecturepattern.DispatcherProvider
import com.example.topratedmoviewitharchitecturepattern.database.MovieDatabase
import com.example.topratedmoviewitharchitecturepattern.ext.mapToDbMovie
import com.example.topratedmoviewitharchitecturepattern.ext.mapToMovie
import com.example.topratedmoviewitharchitecturepattern.model.Movie
import com.example.topratedmoviewitharchitecturepattern.network.MovieApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MovieViewModel(
    private val application: Application,
    private val movieRepository: MovieRepository,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {
    private var movieResponseLiveData: MutableLiveData<List<Movie>> = movieRepository.getMovies()

    fun getMovies(): MutableLiveData<List<Movie>> {
        return movieResponseLiveData
    }

    fun fetchMovies() {
        viewModelScope.launch(dispatchers.io()){
            movieRepository.fetchMovies()
        }

    }

    class Factory(
        private val application: Application,
        private val movieRepository: MovieRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(application, movieRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}