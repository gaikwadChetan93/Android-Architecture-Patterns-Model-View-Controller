package com.example.topratedmoviewitharchitecturepattern.top_rated

import androidx.lifecycle.*
import com.example.topratedmoviewitharchitecturepattern.model.Movie
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun getMovies(): LiveData<List<Movie>> {
        return movieRepository.getMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch(IO) {
            movieRepository.fetchMovies()
        }

    }

    class Factory(
        private val movieRepository: MovieRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(movieRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}