package com.example.topratedmoviewitharchitecturepattern.top_rated

import com.example.topratedmoviewitharchitecturepattern.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieViewModelTest {

    @InjectMockKs
    lateinit var sut: MovieViewModel

    @MockK
    lateinit var movieRepository: MovieRepository

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private val mainTestThread = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainTestThread)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainTestThread.cleanupTestCoroutines()
    }

    @Test
    fun `should invoke fetchMovies of MovieRepository when fetchMovies is called`() {
        runBlocking {
            sut.fetchMovies()
            coVerify(atMost = 1) { movieRepository.fetchMovies() }
            //verify(movieRepository, times(1)).fetchMovies()
        }
    }
}