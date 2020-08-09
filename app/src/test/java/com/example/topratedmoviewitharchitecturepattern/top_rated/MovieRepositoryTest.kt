package com.example.topratedmoviewitharchitecturepattern.top_rated

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.topratedmoviewitharchitecturepattern.database.DBMovie
import com.example.topratedmoviewitharchitecturepattern.database.MovieDao
import com.example.topratedmoviewitharchitecturepattern.database.MovieDatabase
import com.example.topratedmoviewitharchitecturepattern.ext.mapToMovie
import com.example.topratedmoviewitharchitecturepattern.model.Movie
import com.example.topratedmoviewitharchitecturepattern.model.MovieListResponse
import com.example.topratedmoviewitharchitecturepattern.network.MovieApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieRepositoryTest {
    @InjectMockKs
    private lateinit var sut: MovieRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var retrofit: Retrofit

    @MockK
    lateinit var db: MovieDatabase

    @MockK
    lateinit var dbDao: MovieDao

    @MockK
    lateinit var movieApi: MovieApi
    private val cachedMovies = listOf(DBMovie(1, "22", 2.2))
    private val movieResponse = MovieListResponse("1", arrayListOf(Movie(1, "22", 2.2)))
    private val mainTestThread = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { db.movieDao() }.returns(dbDao)
        Dispatchers.setMain(mainTestThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainTestThread.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchMovies is called then return movies from cache if cache is not empty`() {
        runBlockingTest {
            coEvery { dbDao.getAll() }.returns(cachedMovies)
            val movieLiveData = sut.getMovies()
            sut.fetchMovies()

            assertEquals(movieLiveData.value, cachedMovies.mapToMovie())
        }
    }

    @Test
    fun `when fetchMovies is called then return movies from API call response if cache is empty`() {
        runBlockingTest {
            sut = MovieRepository(retrofit, db)
            coEvery { movieApi.fetchTopRatedMovies(any()) }.returns(movieResponse)
            coEvery { dbDao.getAll() }.returns(emptyList())
            every { retrofit.create(MovieApi::class.java) }.returns(movieApi)
            val movieLiveData = sut.getMovies()

            sut.fetchMovies()

            assertEquals(movieLiveData.value, movieResponse.results)

        }
    }

}